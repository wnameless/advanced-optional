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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * 
 * {@link AdvOpt} is basically an Java {@link Optional}. What's more, it
 * contains an additional message to describe the optional value and some
 * convenient methods.
 *
 * @param <T>
 *          the class of the value
 * 
 * @see {@link Optional}
 * 
 */
public final class AdvOpt<T> {

  private static final AdvOpt<?> EMPTY = new AdvOpt<>();

  private final T value;

  private final String message;

  /**
   * Returns a message to describe this optional value.
   * 
   * @return a message to describe this optional value
   */
  public String getMessage() {
    return message;
  }

  /**
   * Returns a translated message to describe this optional value.
   * 
   * @param translator
   *          a function to translate this message
   * @return a translated message to describe this optional value
   */
  public String getMessage(Function<String, String> translator) {
    return translator.apply(message);
  }

  /**
   * Returns true if a message is present, otherwise false.
   * 
   * @return true if a message is present, otherwise false
   */
  public boolean hasMessage() {
    return message != null;
  }

  private AdvOpt() {
    this.value = null;
    this.message = null;
  }

  /**
   * @see {@link Optional#empty()}
   */
  public static <T> AdvOpt<T> empty() {
    @SuppressWarnings("unchecked")
    AdvOpt<T> t = (AdvOpt<T>) EMPTY;
    return t;
  }

  private AdvOpt(T value, String message) {
    this.value = value;
    this.message = message;
  }

  /**
   * @see {@link Optional#of(Object)}
   */
  public static <T> AdvOpt<T> of(T value) {
    return new AdvOpt<>(Objects.requireNonNull(value), null);
  }

  /**
   * Returns an AdvOpt with the specified present non-null value and an optional
   * message.
   * 
   * @param <T>
   *          the class of the value
   * @param value
   *          the value to be present, which must be non-null
   * @param message
   *          a message to describe this optional value
   * @return an AdvOpt with the value present and an optional message
   */
  public static <T> AdvOpt<T> of(T value, String message) {
    return new AdvOpt<>(Objects.requireNonNull(value), message);
  }

  /**
   * Returns an AdvOpt with the specified present value in given Optional.
   * 
   * @param <T>
   *          the class of the value
   * @param optional
   *          the Optional which holds a value to be present, which must be
   *          non-null
   * @return an AdvOpt with the value held by given Optional present
   */
  public static <T> AdvOpt<T> of(Optional<T> optional) {
    Objects.requireNonNull(optional);
    return optional.isPresent() ? AdvOpt.of(optional.get()) : empty();
  }

  /**
   * Returns an AdvOpt with the specified present value in given Optional.
   * 
   * @param <T>
   *          the class of the value
   * @param optional
   *          the Optional which holds a value to be present, which must be
   *          non-null
   * @param message
   *          a message to describe this optional value
   * @return an AdvOpt with the value held by given Optional present and an
   *         optional message
   */
  public static <T> AdvOpt<T> of(Optional<T> optional, String message) {
    Objects.requireNonNull(optional);
    return optional.isPresent() ? AdvOpt.of(optional.get(), message)
        : AdvOpt.ofNullable(null, message);
  }

  /**
   * Creates a Java Optional based on this {@link AdvOpt} value.
   * 
   * @return a Java Optional based on this {@link AdvOpt} value
   */
  public Optional<T> toOptional() {
    return isPresent() ? Optional.of(value) : Optional.empty();
  }

  /**
   * @see {@link Optional#ofNullable(Object)}
   */
  public static <T> AdvOpt<T> ofNullable(T value) {
    return new AdvOpt<>(value, null);
  }

  /**
   * Returns an AdvOpt describing the specified value, if non-null, otherwise
   * returns an empty AdvOpt.
   * 
   * @param <T>
   *          the class of the value
   * @param value
   *          the possibly-null value to describe
   * @param message
   *          a message to describe this optional value
   * @return an AdvOpt with a present value if the specified value is non-null,
   *         otherwise an empty AdvOpt
   */
  public static <T> AdvOpt<T> ofNullable(T value, String message) {
    return new AdvOpt<>(value, message);
  }

  /**
   * @see {@link Optional#get()}
   */
  public T get() {
    if (value == null) {
      throw new NoSuchElementException("No value present");
    }
    return value;
  }

  /**
   * @see {@link Optional#isPresent()}
   */
  public boolean isPresent() {
    return value != null;
  }

  /**
   * Return true if there is a value absent, otherwise false.
   * 
   * @return true if there is a value absent, otherwise false
   */
  public boolean isAbsent() {
    return value == null;
  }

  /**
   * @see {@link Optional#ifPresent(Consumer)}
   */
  public void ifPresent(Consumer<? super T> action) {
    if (value != null) {
      action.accept(value);
    }
  }

  /**
   * @see {@link Optional#ifPresentOrElse(Consumer)}
   */
  public void ifPresentOrElse(Consumer<? super T> action,
      Runnable emptyAction) {
    if (value != null) {
      action.accept(value);
    } else {
      emptyAction.run();
    }
  }

  /**
   * @see {@link Optional#filter(Predicate)}
   */
  public AdvOpt<T> filter(Predicate<? super T> predicate) {
    Objects.requireNonNull(predicate);
    if (!isPresent()) {
      return this;
    } else {
      return predicate.test(value) ? this : empty();
    }
  }

  /**
   * @see {@link Optional#map(Function)}
   */
  public <U> AdvOpt<U> map(Function<? super T, ? extends U> mapper) {
    Objects.requireNonNull(mapper);
    if (!isPresent()) {
      return empty();
    } else {
      return AdvOpt.ofNullable(mapper.apply(value));
    }
  }

  /**
   * @see {@link Optional#flatMap(Function)}
   */
  public <U> AdvOpt<U> flatMap(
      Function<? super T, ? extends AdvOpt<? extends U>> mapper) {
    Objects.requireNonNull(mapper);
    if (!isPresent()) {
      return empty();
    } else {
      @SuppressWarnings("unchecked")
      AdvOpt<U> r = (AdvOpt<U>) mapper.apply(value);
      return Objects.requireNonNull(r);
    }
  }

  /**
   * @see {@link Optional#or(Supplier)}
   */
  public AdvOpt<T> or(Supplier<? extends AdvOpt<? extends T>> supplier) {
    Objects.requireNonNull(supplier);
    if (isPresent()) {
      return this;
    } else {
      @SuppressWarnings("unchecked")
      AdvOpt<T> r = (AdvOpt<T>) supplier.get();
      return Objects.requireNonNull(r);
    }
  }

  /**
   * @see {@link Optional#stream()}
   */
  public Stream<T> stream() {
    if (!isPresent()) {
      return Stream.empty();
    } else {
      return Stream.of(value);
    }
  }

  /**
   * @see {@link Optional#orElse(T)}
   */
  public T orElse(T other) {
    return value != null ? value : other;
  }

  /**
   * @see {@link Optional#orElseGet(Supplier)}
   */
  public T orElseGet(Supplier<? extends T> supplier) {
    return value != null ? value : supplier.get();
  }

  /**
   * @see {@link Optional#orElseThrow(Supplier)}
   */
  public <X extends Throwable> T orElseThrow(
      Supplier<? extends X> exceptionSupplier) throws X {
    if (value != null) {
      return value;
    } else {
      throw exceptionSupplier.get();
    }
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (!(obj instanceof AdvOpt)) {
      return false;
    }

    AdvOpt<?> other = (AdvOpt<?>) obj;
    return Objects.equals(value, other.value)
        && Objects.equals(message, other.message);
  }

  @Override
  public int hashCode() {
    return Objects.hash(value, message);
  }

  @Override
  public String toString() {
    return value != null || message != null
        ? String.format("AdvOpt[value=%s, message=%s]", value, message)
        : "AdvOpt.empty";
  }

}
