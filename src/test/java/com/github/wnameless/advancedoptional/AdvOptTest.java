/*
 *
 * Copyright 2019 Wei-Ming Wu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */
package com.github.wnameless.advancedoptional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

public class AdvOptTest {

  AdvOpt<String> valueButMsg;
  AdvOpt<String> valueAndMsg;
  AdvOpt<String> nullAndMsg;
  AdvOpt<String> nullButMsg;
  AdvOpt<String> nullAndNull;

  @Before
  public void init() {
    valueButMsg = AdvOpt.of("Test");
    valueAndMsg = AdvOpt.of("Test", "Msg");
    nullAndMsg = AdvOpt.ofNullable(null, "Msg");
    nullButMsg = AdvOpt.ofNullable(null);
    nullAndNull = AdvOpt.ofNullable(null, null);
  }

  @Test
  public void testIsAbsent() {
    assertFalse(valueButMsg.isAbsent());
    assertFalse(valueAndMsg.isAbsent());
    assertTrue(nullAndMsg.isAbsent());
    assertTrue(nullButMsg.isAbsent());
    assertTrue(nullAndNull.isAbsent());
  }

  @Test
  public void testHasMessage() {
    assertFalse(valueButMsg.hasMessage());
    assertTrue(valueAndMsg.hasMessage());
    assertTrue(nullAndMsg.hasMessage());
    assertFalse(nullButMsg.hasMessage());
    assertFalse(nullAndNull.hasMessage());
  }

  @Test
  public void testGetMessage() {
    assertEquals(null, valueButMsg.getMessage());
    assertEquals("Msg", valueAndMsg.getMessage());
    assertEquals("Msg", nullAndMsg.getMessage());
    assertEquals(null, nullButMsg.getMessage());
    assertEquals(null, nullAndNull.getMessage());
  }

  @Test
  public void testGetMessageWithTranslator() {
    assertEquals("msg", valueAndMsg.getMessage(String::toLowerCase));
    assertEquals("MSG", nullAndMsg.getMessage(String::toUpperCase));
  }

  @Test
  public void testOfOptional() {
    assertEquals(valueButMsg, AdvOpt.of(Optional.of("Test")));
    assertNotEquals(valueAndMsg, AdvOpt.of(Optional.of("Test")));
    assertEquals(valueAndMsg, AdvOpt.of(Optional.of("Test"), "Msg"));
    assertNotEquals(nullAndMsg, AdvOpt.of(Optional.ofNullable(null)));
    assertEquals(nullAndMsg, AdvOpt.of(Optional.ofNullable(null), "Msg"));
    assertEquals(nullButMsg, AdvOpt.of(Optional.ofNullable(null)));
    assertEquals(nullAndNull, AdvOpt.of(Optional.ofNullable(null)));
  }

  @Test
  public void toOptional() {
    assertEquals(Optional.of("Test"), valueButMsg.toOptional());
    assertEquals(Optional.of("Test"), valueAndMsg.toOptional());
    assertSame(Optional.empty(), nullAndMsg.toOptional());
    assertSame(Optional.empty(), nullButMsg.toOptional());
    assertSame(Optional.empty(), nullAndNull.toOptional());
  }

  @Test
  public void testIdenticalEmpty() {
    assertSame(AdvOpt.empty(), AdvOpt.empty());
  }

  @Test
  public void testEquals() {
    assertEquals(AdvOpt.empty(), AdvOpt.empty());
    assertNotEquals(AdvOpt.empty(), Optional.empty());

    assertEquals(AdvOpt.of("Test"), valueButMsg);
    assertEquals(AdvOpt.of("Test", "Msg"), valueAndMsg);
    assertEquals(AdvOpt.ofNullable(null, "Msg"), nullAndMsg);
    assertEquals(AdvOpt.ofNullable(null), nullButMsg);
    assertEquals(AdvOpt.ofNullable(null, null), nullAndNull);
    assertEquals(AdvOpt.empty(), nullAndNull);

    assertNotEquals(AdvOpt.of("test", "Msg"), valueAndMsg);
    assertNotEquals(AdvOpt.of("Test", "msg"), valueAndMsg);
  }

  @Test
  public void testHashCode() {
    assertEquals(AdvOpt.of("Test").hashCode(), valueButMsg.hashCode());
    assertEquals(AdvOpt.of("Test", "Msg").hashCode(), valueAndMsg.hashCode());
    assertEquals(AdvOpt.ofNullable(null, "Msg").hashCode(),
        nullAndMsg.hashCode());
    assertEquals(AdvOpt.ofNullable(null).hashCode(), nullButMsg.hashCode());
    assertEquals(AdvOpt.ofNullable(null, null).hashCode(),
        nullAndNull.hashCode());
    assertEquals(AdvOpt.empty().hashCode(), nullAndNull.hashCode());
  }

  @Test
  public void testToString() {
    assertEquals("AdvOpt[value=Test, message=null]", valueButMsg.toString());
    assertEquals("AdvOpt[value=Test, message=Msg]", valueAndMsg.toString());
    assertEquals("AdvOpt[value=null, message=Msg]", nullAndMsg.toString());
    assertEquals("AdvOpt.empty", nullButMsg.toString());
    assertEquals("AdvOpt.empty", nullAndNull.toString());
    assertEquals("AdvOpt.empty", AdvOpt.empty().toString());
  }

  // Tests of original Optional methods
  @Test(expected = NoSuchElementException.class)
  public void testGetException() {
    nullAndMsg.get();
  }

  @Test
  public void testGet() {
    assertEquals("Test", valueButMsg.get());
  }

  @Test
  public void testIsPresent() {
    assertTrue(valueButMsg.isPresent());
    assertFalse(nullButMsg.isPresent());
  }

  @Test
  public void testIfPresent() {
    final int[] counter = new int[] { 0 };
    valueButMsg.ifPresent(val -> counter[0]++);
    assertEquals(1, counter[0]);
    nullButMsg.ifPresent(val -> counter[0]++);
    assertEquals(1, counter[0]);
  }

  @Test
  public void testIfPresentOrElse() {
    final int[] counter = new int[] { 0 };
    valueButMsg.ifPresentOrElse(val -> counter[0]++, () -> counter[0] += 2);
    assertEquals(1, counter[0]);
    nullButMsg.ifPresentOrElse(val -> counter[0]++, () -> counter[0] += 2);
    assertEquals(3, counter[0]);
  }

  @Test
  public void testFilter() {
    assertSame(nullButMsg, nullButMsg.filter(val -> val != null));
    assertSame(valueButMsg, valueButMsg.filter(val -> val.equals("Test")));
    assertSame(AdvOpt.empty(), valueButMsg.filter(val -> val.equals("test")));
  }

  @Test
  public void testMap() {
    assertSame(AdvOpt.empty(), nullButMsg.map(val -> val.length()));
    assertEquals(AdvOpt.of(4), valueButMsg.map(val -> val.length()));
  }

  @Test
  public void testFlatMap() {
    assertSame(AdvOpt.empty(),
        nullButMsg.flatMap(val -> AdvOpt.of(val.length())));
    assertEquals(AdvOpt.of(4),
        valueButMsg.flatMap(val -> AdvOpt.of(val.length())));
  }

  @Test
  public void testOr() {
    assertSame(valueButMsg, valueButMsg.or(() -> AdvOpt.of("Or")));
    assertEquals(AdvOpt.of("Or"), nullButMsg.or(() -> AdvOpt.of("Or")));
  }

  @Test
  public void testStream() {
    assertEquals(Stream.empty().collect(Collectors.toList()),
        nullButMsg.stream().collect(Collectors.toList()));
    assertEquals(Stream.of("Test").collect(Collectors.toList()),
        valueButMsg.stream().collect(Collectors.toList()));
  }

  @Test
  public void testOrElse() {
    assertEquals("Test", valueButMsg.orElse("TEST"));
    assertEquals("TEST", nullButMsg.orElse("TEST"));
  }

  @Test
  public void testOrElseGet() {
    assertEquals("Test", valueButMsg.orElseGet(() -> "TEST"));
    assertEquals("TEST", nullButMsg.orElseGet(() -> "TEST"));
  }

  @Test
  public void testOrElseThrow() {
    assertEquals("Test", valueButMsg.orElseThrow(() -> new RuntimeException()));
  }

  @Test(expected = RuntimeException.class)
  public void testOrElseThrowException() {
    nullButMsg.orElseThrow(() -> new RuntimeException());
  }

}
