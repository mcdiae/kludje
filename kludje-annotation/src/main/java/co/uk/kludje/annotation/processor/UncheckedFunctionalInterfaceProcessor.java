/*
Copyright 2014 McDowell

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package co.uk.kludje.annotation.processor;

import co.uk.kludje.annotation.UncheckedFunctionalInterface;
import co.uk.kludje.annotation.UncheckedFunctionalInterfaces;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

/**
 * Annotation processor for {@link co.uk.kludje.annotation.UncheckedFunctionalInterface}.
 * Generated types will be placed into the annotated package.
 *
 * @see co.uk.kludje.annotation.UncheckedFunctionalInterface
 */
public class UncheckedFunctionalInterfaceProcessor extends AbstractProcessor {
  private static final String NAME = "FIP: ";
  private static final Generator INTERFACE = InterfaceGenerator.load();

  @Override
  public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
    if (roundEnv.processingOver() || annotations.size() == 0) {
      return false;
    }

    for (Element target : roundEnv.getElementsAnnotatedWith(UncheckedFunctionalInterfaces.class)) {
      Collection<AnnotationMirror> mirrors = new ArrayList<>();
      for (AnnotationMirror mirror : target.getAnnotationMirrors()) {
        if (is(mirror, UncheckedFunctionalInterfaces.class)) {
          AnnotationValue values = value(mirror, "value");
          @SuppressWarnings("unchecked")
          Collection<AnnotationMirror> coll = (Collection<AnnotationMirror>) values.getValue();
          processPackage((PackageElement) target, coll);
          break;
        }
      }
      processPackage((PackageElement) target, mirrors);
    }

    for (Element target : roundEnv.getElementsAnnotatedWith(UncheckedFunctionalInterface.class)) {
      for (AnnotationMirror mirror : target.getAnnotationMirrors()) {
        if (is(mirror, UncheckedFunctionalInterface.class)) {
          Collection<AnnotationMirror> coll = asList(mirror);
          processPackage((PackageElement) target, coll);
          break;
        }
      }
    }

    return true;
  }

  private <A extends Annotation> boolean is(AnnotationMirror mirror, Class<A> type) {
    TypeElement te = (TypeElement) mirror.getAnnotationType().asElement();
    return te.getQualifiedName().toString().equals(type.getName());
  }

  private AnnotationValue value(AnnotationMirror mirror, String name) {
    for (Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : mirror.getElementValues().entrySet()) {
      if (name.equals(entry.getKey().getSimpleName().toString())) {
        return entry.getValue();
      }
    }
    return null;
  }

  private void processPackage(PackageElement pack, Collection<AnnotationMirror> ufis) {
    if (ufis.isEmpty()) {
      return;
    }
    info("Processing " + pack.getQualifiedName());
    for (AnnotationMirror ufi : ufis) {
      TypeElement type = type(ufi);
      validateUfi(type);
      try {
        writeUfi(pack, type);
      } catch (IOException e) {
        err(e);
      }
    }
  }

  private TypeElement type(AnnotationMirror ufi) {
    AnnotationValue value = value(ufi, "value");
    TypeMirror typeMirror = (TypeMirror) value.getValue();
    return (TypeElement) processingEnv.getTypeUtils().asElement(typeMirror);
  }

  private void validateUfi(TypeElement te) {
    ensure(this.processingEnv.getElementUtils().isFunctionalInterface(te), "assert is functional interface", te.getQualifiedName().toString());
  }

  private void writeUfi(PackageElement pack, TypeElement type) throws IOException {
    String simpleName = "U" + type.getSimpleName();

    info("Generating " + pack.getQualifiedName() + "." + simpleName);

    Map<String, String> params = params(pack, type, simpleName, findFunctionalMethod(type));

    JavaFileObject file = this.processingEnv.getFiler()
        .createSourceFile(pack.getQualifiedName() + "." + simpleName);
    try (Writer w = file.openWriter()) {
      INTERFACE.write(w, params);
    }
  }

  private Map<String, String> params(PackageElement pack, TypeElement type, String simpleName, ExecutableElement method) {
    Map<String, String> map = new HashMap<>();
    map.put("pack", pack.getQualifiedName().toString());
    map.put("generator", UncheckedFunctionalInterfaceProcessor.class.getName());
    map.put("simpleName", simpleName);
    map.put("parent", type.getQualifiedName().toString());
    map.put("generics", inferTypeGenerics(type, true));
    map.put("parentGenerics", inferTypeGenerics(type, false));
    map.put("parentSig", methodSignature(method, ""));
    map.put("parentSigGenerics", "");
    map.put("functionSignature", methodSignature(method, "$"));
    map.put("invocation", invocation(method));
    return map;
  }

  private String methodSignature(ExecutableElement method, String prefix) {
    String result = "";
    if (!method.getTypeParameters().isEmpty()) {
      result += "<" + method.getTypeParameters() + "> ";
    }
    result += method.getReturnType() + " "
        + prefix
        + method.getSimpleName() + "(";
    if (!method.getParameters().isEmpty()) {
      StringBuilder buf = new StringBuilder();
      for (VariableElement element : method.getParameters()) {
        buf.append(buf.length() == 0 ? "" : ", ")
            .append(element.asType())
            .append(" ")
            .append(element);
      }
      result += buf.toString();
    }
    return result + ")";
  }

  private String invocation(ExecutableElement method) {
    String result = "";
    if (!"void".equals(method.getReturnType().toString())) {
      result += "return ";
    }
    result += "$" + method.getSimpleName() + "(" + method.getParameters() + ")";
    return result;
  }

  private String inferTypeGenerics(TypeElement type, boolean withConstraints) {
    List<? extends TypeParameterElement> elements = type.getTypeParameters();
    if (elements.isEmpty()) {
      return "";
    }
    StringBuilder buf = new StringBuilder("<");
    for (TypeParameterElement param : elements) {
      if (buf.length() > 1) {
        buf.append(", ");
      }
      buf.append(param.toString());
      if (withConstraints) {
        for (TypeMirror bound : param.getBounds()) {
          TypeElement element = (TypeElement) this.processingEnv.getTypeUtils().asElement(bound);
          if (!element.getQualifiedName().toString().equals("java.lang.Object")) {
            buf.append(" extends ").append(bound);
          }
        }
      }
    }
    return buf.append(">").toString();
  }

  private ExecutableElement findFunctionalMethod(TypeElement type) {
    List<ExecutableElement> methods = abstractMethods(type)
        .collect(Collectors.toList());
    info(methods.toString());
    if (methods.size() != 1) {
      throw new IllegalStateException(methods.toString());
    }
    return methods.get(0);
  }

  private Stream<TypeElement> allSubTypes(TypeElement type) {
    Stream<TypeElement> sub = type.getInterfaces()
        .stream()
        .map(m -> processingEnv.getTypeUtils().asElement(m))
        .map(t -> (TypeElement) t)
        .flatMap(this::allSubTypes);
    return Stream.concat(sub, Stream.of(type));
  }

  private Stream<TypeElement> typeHierarch(TypeElement type) {
    Stream<TypeElement> stream = allSubTypes(type);
    List<TypeElement> list = stream.collect(Collectors.toList());
    info("types: " + list.toString());
    return list.stream();
  }

  private Stream<ExecutableElement> allMethods(TypeElement type) {
    return typeHierarch(type)
        .flatMap(t -> t.getEnclosedElements().stream().filter(x -> {
          info(t + " " + x.getKind() + " " + x);
          return true;
        }))
        .filter(t -> t.getKind() == ElementKind.METHOD)
        .map(t -> (ExecutableElement) t);
  }

  private <T> Predicate<T> filter(Collection<T> set, BiPredicate<T, T> bp) {
    return t -> set
        .stream()
        .anyMatch(m -> bp.test(m, t));
  }

  private Stream<ExecutableElement> methods(TypeElement type) {
    Set<ExecutableElement> all = allMethods(type)
        .filter(e -> !e.getModifiers().contains(Modifier.STATIC))
        .collect(Collectors.toCollection(HashSet::new));
    info("methods: " + all);
    Predicate<ExecutableElement> overridden = filter(all, (m, t) ->
        m != t && processingEnv.getElementUtils().overrides(t, m, type));
    return all
        .stream()
        .filter(overridden.negate());
  }

  private Stream<ExecutableElement> abstractMethods(TypeElement type) {
    return methods(type)
        .filter(e -> !e.getModifiers().contains(Modifier.DEFAULT));
  }

  @Override
  public Set<String> getSupportedAnnotationTypes() {
    return new HashSet<>(asList(UncheckedFunctionalInterface.class.getName(),
        UncheckedFunctionalInterfaces.class.getName()));
  }

  @Override
  public SourceVersion getSupportedSourceVersion() {
    return SourceVersion.RELEASE_8;
  }

  private void ensure(boolean predicate, String rule, String hint) {
    if (!predicate) {
      String err = UncheckedFunctionalInterfaces.class.getName() + "; FAILED: " + rule + ": " + hint;
      processingEnv.getMessager()
          .printMessage(Diagnostic.Kind.ERROR, err);
    }
  }

  private void err(Throwable t) {
    processingEnv.getMessager()
        .printMessage(Diagnostic.Kind.ERROR, t.toString());
  }

  private void info(String str) {
    processingEnv.getMessager()
        .printMessage(Diagnostic.Kind.NOTE, NAME + str);
  }

}
