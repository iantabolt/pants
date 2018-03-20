package org.pantsbuild.tools.junit.impl;

import org.junit.runner.Runner;

public final class ScalaTestUtil {
  private ScalaTestUtil() {}

  /**
   * org.scalatest.Suite.class, loaded with runtime reflection to avoid the
   * scalatest dependency.
   */
  private static final Class<?> suiteClass;
  static {
    try {
      suiteClass = Class.forName("org.scalatest.Suite");
    } catch (ClassNotFoundException e) {
      // No scalatest tests on classpath
      suiteClass = null;
    }
  }

  /**
   * Returns a scalatest junit runner using reflection in the classloader of the test.
   * @param clazz the test class
   *
   * @return a new scala test junit runner
   */
  public static Runner getJUnitRunner(Class<?> clazz) {
    try {
      Class<?> junitRunnerClass = Class.forName("org.scalatest.junit.JUnitRunner",
          true, clazz.getClassLoader());
      return (Runner)junitRunnerClass.getConstructor(Class.class).newInstance(clazz);
    } catch (Exception e) {
      // isScalaTest should fail if scala test isn't available so this is probably ok.
      throw new RuntimeException(e);
    }
  }

  /**
   * Checks if the passed in test clazz has an ancestor that is the scala test suite
   * object (looked up in the test classes class loader).
   * @param clazz the test class
   *
   * @return true if the test class is a scalatest test, false if not.
   */
  public static boolean isScalaTestTest(Class<?> clazz) {
    return suiteClass != null && suiteClass.isAssignableFrom(clazz);
  }
}
