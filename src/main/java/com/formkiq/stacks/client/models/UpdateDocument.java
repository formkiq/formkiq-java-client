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

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;

/**
 * 
 * {@link UpdateDocument} holder.
 *
 */
public class UpdateDocument {

  /** Path. */
  private String path;
  /** Content Type. */
  private String contentType;
  /** Document Content. */
  private String content;
  /** Is Content Base 64 Encoded. */
  private boolean isBase64;
  /** Document Tags. */
  private List<DocumentTag> tags;
  /** Child Documents. */
  private List<UpdateDocument> documents;

  /**
   * constructor.
   */
  public UpdateDocument() {}

  /**
   * Get Content.
   * 
   * @return {@link String}
   */
  public String content() {
    return this.content;
  }

  /**
   * Set Content.
   * 
   * @param s {@link String}
   * @return {@link UpdateDocument}
   */
  public UpdateDocument content(final String s) {
    this.content = s;
    isBase64(false);
    return this;
  }

  /**
   * Set Content and converts to Base64.
   * 
   * @param s {@link String}
   * @param charset {@link Charset}
   * @return {@link UpdateDocument}
   */
  public UpdateDocument content(final String s, final Charset charset) {
    return contentAsBase64(Base64.getEncoder().encodeToString(s.getBytes(charset)));
  }

  /**
   * Set Content.
   * 
   * @param s Base64 encoded string.
   * @return {@link UpdateDocument}
   */
  public UpdateDocument contentAsBase64(final String s) {
    this.content = s;
    isBase64(true);
    return this;
  }

  /**
   * Get Content Type.
   * 
   * @return {@link String}
   */
  public String contentType() {
    return this.contentType;
  }

  /**
   * Set Content Type.
   * 
   * @param s {@link String}
   * @return {@link UpdateDocument}
   */
  public UpdateDocument contentType(final String s) {
    this.contentType = s;
    isBase64(false);
    return this;
  }

  /**
   * Child Documents.
   * 
   * @return {@link List} {@link UpdateDocument}
   */
  public List<UpdateDocument> documents() {
    return this.documents;
  }

  /**
   * Get Child Documents.
   * 
   * @param list {@link List} {@link UpdateDocument}
   * @return {@link UpdateDocument}
   */
  public UpdateDocument documents(final List<UpdateDocument> list) {
    this.documents = list;
    return this;
  }

  /**
   * Is Content Base64 Encoded.
   * 
   * @return boolean
   */
  public boolean isBase64() {
    return this.isBase64;
  }

  /**
   * Set Content as Base64 Encoded.
   * 
   * @param base64 boolean
   * @return {@link UpdateDocument}
   */
  public UpdateDocument isBase64(final boolean base64) {
    this.isBase64 = base64;
    return this;
  }

  /**
   * Get Path.
   * 
   * @return {@link String}
   */
  public String path() {
    return this.path;
  }

  /**
   * Set Path.
   * 
   * @param s {@link String}
   * @return {@link UpdateDocument}
   */
  public UpdateDocument path(final String s) {
    this.path = s;
    return this;
  }

  /**
   * Get Tags.
   * 
   * @return {@link List} {@link DocumentTag}
   */
  public List<DocumentTag> tags() {
    return this.tags;
  }

  /**
   * Set Tags.
   * 
   * @param list {@link List} {@link DocumentTag}
   * @return {@link UpdateDocument}
   */
  public UpdateDocument tags(final List<DocumentTag> list) {
    this.tags = list;
    return this;
  }
}
