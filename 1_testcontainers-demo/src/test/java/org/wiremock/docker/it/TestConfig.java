package org.wiremock.docker.it;

import java.io.File;
import java.nio.file.Path;

/**
 * WireMock Test configuration.
 * To be set by the test runner to configure proper execution.
 */
public class TestConfig {

  /**
   * Docker image tag to be used for testing.
   */
  public static String WIREMOCK_IMAGE =
    System.getProperty("it.wiremock-image", "wiremock/wiremock:test");

  public static String SAMPLES_DIR =
    System.getProperty("it.samples-path", "./samples");

  public static boolean isWebkooksExtensionEmbedded() {
    // TODO Fixme
    return true;
  }

  public static Path getSamplesPath() {
    return new File(SAMPLES_DIR).toPath();
  }
}
