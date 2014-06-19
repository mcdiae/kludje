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

package uk.kludje.annotation.processor;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.util.Arrays;
import java.util.List;

class MethodSpec {

  private static final List<MethodSpec> OBJECT = Arrays.asList(
      new MethodSpec("toString"),
      new MethodSpec("equals", "java.lang.Object"),
      new MethodSpec("hashCode"),
      new MethodSpec("finalize"),
      new MethodSpec("clone")
  );

  private final String name;
  private final List<String> params;

  private MethodSpec(String name, String... params) {
    this.name = name;
    this.params = Arrays.asList(params);
  }

  public static boolean isObjectMethod(ProcessingEnvironment env, ExecutableElement method) {
    return OBJECT.stream()
        .anyMatch(spec -> spec.match(env, method));
  }

  private boolean match(ProcessingEnvironment env, ExecutableElement method) {
    return name.equals(method.getSimpleName().toString())
        && paramMatch(env, method);
  }

  private boolean paramMatch(ProcessingEnvironment env, ExecutableElement method) {
    List<? extends VariableElement> mps = method.getParameters();
    if (mps.size() != params.size()) {
      return false;
    }
    for (int i = 0; i < params.size(); i++) {
      VariableElement ve = mps.get(i);
      TypeElement type = (TypeElement) env.getTypeUtils().asElement(ve.asType());
      if (!params.get(i).equals(type.getQualifiedName().toString())) {
        return false;
      }
    }
    return true;
  }
}
