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
package com.formkiq.stacks.client.models;

/**
 * DocumentUrl holder.
 *
 */
public class DocumentUrl {

  /** Document Url. */
  private String url;
  /** Document Id. */
  private String documentId;

  /**
   * constructor.
   */
  public DocumentUrl() {

  }

  /**
   * Get Document Url.
   * 
   * @return {@link String}
   */
  public String url() {
    return this.url;
  }

  /**
   * Set Document Url.
   * 
   * @param documentUrl {@link String}
   */
  public void url(final String documentUrl) {
    this.url = documentUrl;
  }

  /**
   * Get Document Id.
   * 
   * @return {@link String}
   */
  public String documentId() {
    return this.documentId;
  }

  /**
   * Set Document Id.
   * 
   * @param id {@link String}
   */
  public void documentId(final String id) {
    this.documentId = id;
  }
}
