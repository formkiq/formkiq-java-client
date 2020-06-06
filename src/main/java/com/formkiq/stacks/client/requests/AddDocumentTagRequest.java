/**
 * Copyright [2020] FormKiQ Inc. Licensed under the Apache License, Version 2.0 (the "License"); you
 * may not use this file except in compliance with the License. You may obtain a copy of the License
 * at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.formkiq.stacks.client.requests;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.formkiq.stacks.client.ApiRequest;
import com.formkiq.stacks.client.Validate;

/**
 * Creates POST /documents/{documentId}/tags Request.
 *
 */
public class AddDocumentTagRequest implements ApiRequest {

  /** Tag Key. */
  private String tagKey;
  /** Tag Value. */
  private String tagValue;
  /** Request Parameters. */
  private Map<String, String> paths = new HashMap<>();
  /** Request Parameters. */
  private Map<String, String> parameters = new HashMap<>();

  /**
   * constructor.
   */
  public AddDocumentTagRequest() {}

  /**
   * Set the DocumentId.
   * 
   * @param documentId {@link String}
   * @return {@link AddDocumentTagRequest}
   */
  public AddDocumentTagRequest documentId(final String documentId) {
    this.paths.put("documentId", documentId);
    return this;
  }

  @Override
  public Map<String, String> getQueryParameters() {
    return Collections.emptyMap();
  }

  @Override
  public String getUrlPath() {
    return "documents/" + this.paths.get("documentId") + "/tags";
  }

  /**
   * Site Identifier.
   * 
   * @param siteId {@link String}
   * @return {@link AddDocumentTagRequest}
   */
  public AddDocumentTagRequest siteId(final String siteId) {
    this.parameters.put("siteId", siteId);
    return this;
  }

  /**
   * Get Tag Key.
   * 
   * @return {@link String}
   */
  public String tagKey() {
    return this.tagKey;
  }

  /**
   * Set Tag Key.
   * 
   * @param s {@link String}
   * @return {@link AddDocumentTagRequest}
   */
  public AddDocumentTagRequest tagKey(final String s) {
    this.tagKey = s;
    return this;
  }

  /**
   * Get Tag Value.
   * 
   * @return {@link String}
   */
  public String tagValue() {
    return this.tagValue;
  }

  /**
   * Set Tag Value.
   * 
   * @param s {@link String}
   * @return {@link AddDocumentTagRequest}
   */
  public AddDocumentTagRequest tagValue(final String s) {
    this.tagValue = s;
    return this;
  }

  @Override
  public void validate() {
    Validate.notNull(this.paths.get("documentId"), "DocumentId is required.");
    Validate.notNull(this.tagKey, "TagKey is required.");
    Validate.notNull(this.tagValue, "TagValue is required.");
  }
}
