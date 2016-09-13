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

/**
 * <p>N-ary functional interfaces.
 * That is, function and consumer implementations that take up to 7 arguments.</p>
 *
 * <table summary="n-ary functional interfaces">
 *   <tr>
 *     <th>Arity</th>
 *     <th>Function</th>
 *     <th>Consumer</th>
 *   </tr>
 *   <tr>
 *     <td>0</td>
 *     <td>{@link java.util.function.Supplier}</td>
 *     <td>{@link java.lang.Runnable}</td>
 *   </tr>
 *   <tr>
 *     <td>1</td>
 *     <td>{@link java.util.function.Function}</td>
 *     <td>{@link java.util.function.Consumer}</td>
 *   </tr>
 *   <tr>
 *     <td>2</td>
 *     <td>{@link java.util.function.BiFunction}</td>
 *     <td>{@link java.util.function.BiConsumer}</td>
 *   </tr>
 *   <tr>
 *     <td>3</td>
 *     <td>{@link uk.kludje.fn.nary.TriFunction}</td>
 *     <td>{@link uk.kludje.fn.nary.TriConsumer}</td>
 *   </tr>
 *   <tr>
 *     <td>4</td>
 *     <td>{@link uk.kludje.fn.nary.TetraFunction}</td>
 *     <td>{@link uk.kludje.fn.nary.TetraConsumer}</td>
 *   </tr>
 *   <tr>
 *     <td>5</td>
 *     <td>{@link uk.kludje.fn.nary.PentaFunction}</td>
 *     <td>{@link uk.kludje.fn.nary.PentaConsumer}</td>
 *   </tr>
 *   <tr>
 *     <td>6</td>
 *     <td>{@link uk.kludje.fn.nary.HexaFunction}</td>
 *     <td>{@link uk.kludje.fn.nary.HexaConsumer}</td>
 *   </tr>
 *   <tr>
 *     <td>7</td>
 *     <td>{@link uk.kludje.fn.nary.SeptaFunction}</td>
 *     <td>{@link uk.kludje.fn.nary.SeptaConsumer}</td>
 *   </tr>
 * </table>
 */
@UncheckedFunctionalInterface(TriConsumer.class)
@UncheckedFunctionalInterface(TetraConsumer.class)
@UncheckedFunctionalInterface(PentaConsumer.class)
@UncheckedFunctionalInterface(HexaConsumer.class)
@UncheckedFunctionalInterface(SeptaConsumer.class)
@UncheckedFunctionalInterface(TriFunction.class)
@UncheckedFunctionalInterface(TetraFunction.class)
@UncheckedFunctionalInterface(PentaFunction.class)
@UncheckedFunctionalInterface(HexaFunction.class)
@UncheckedFunctionalInterface(SeptaFunction.class) package uk.kludje.fn.nary;

import uk.kludje.annotation.UncheckedFunctionalInterface;
