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

import java.util.List;

/**
 * 
 * {@link DocumentVersions} holder.
 *
 */
public class DocumentVersions {

  /** Path. */
  private String next;
  /** Content Type. */
  private List<DocumentVersion> versions;

  /**
   * constructor.
   */
  public DocumentVersions() {}

  /**
   * Get Next Token.
   * 
   * @return {@link String}
   */
  public String next() {
    return this.next;
  }

  /**
   * Set Next Token.
   * 
   * @param token {@link String}
   */
  public void next(final String token) {
    this.next = token;
  }

  /**
   * Get Document Versions.
   * 
   * @return {@link List} {@link DocumentVersion}
   */
  public List<DocumentVersion> versions() {
    return this.versions;
  }

  /**
   * Get Document Versions.
   * 
   * @param list {@link List} {@link DocumentVersion}
   */
  public void versions(final List<DocumentVersion> list) {
    this.versions = list;
  }
}
