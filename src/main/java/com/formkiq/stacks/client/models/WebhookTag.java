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

import java.util.Date;

/**
 * WebhookTag holder.
 *
 */
public class WebhookTag {

  /** Webhook Tag Key. */
  private String key;
  /** Webhook Tag Value. */
  private String value;
  /** Webhook Tag WebhookId. */
  private String webhookId;
  /** Webhook Tag Type. */
  private String type;
  /** Webhook Tag UserId. */
  private String userId;
  /** Webhook Tag InsertedDate. */
  private Date insertedDate;

  /**
   * constructor.
   */
  public WebhookTag() {

  }

  /**
   * Get Tag WebhookId.
   * 
   * @return {@link String}
   */
  public String webhookId() {
    return this.webhookId;
  }

  /**
   * Set Tag WebhookId.
   * 
   * @param id {@link String}
   * @return {@link WebhookTag}
   */
  public WebhookTag webhookId(final String id) {
    this.webhookId = id;
    return this;
  }

  /**
   * Get Tag Inserted Date.
   * 
   * @return {@link Date}
   */
  public Date insertedDate() {
    return this.insertedDate;
  }

  /**
   * Set Inserted Date.
   * 
   * @param date {@link Date}
   * @return {@link WebhookTag}
   */
  public WebhookTag insertedDate(final Date date) {
    this.insertedDate = date;
    return this;
  }

  /**
   * Get Tag Key.
   * 
   * @return {@link String}
   */
  public String key() {
    return this.key;
  }

  /**
   * Set Key.
   * 
   * @param tagkey {@link String}
   * @return {@link WebhookTag}
   */
  public WebhookTag key(final String tagkey) {
    this.key = tagkey;
    return this;
  }

  /**
   * Get Tag Type.
   * 
   * @return {@link String}
   */
  public String type() {
    return this.type;
  }

  /**
   * Set Tag Type.
   * 
   * @param tagType {@link String}
   * @return {@link WebhookTag}
   */
  public WebhookTag type(final String tagType) {
    this.type = tagType;
    return this;
  }

  /**
   * Get Tag UserId.
   * 
   * @return {@link String}
   */
  public String userId() {
    return this.userId;
  }

  /**
   * Set Tag UserId.
   * 
   * @param tagUserId {@link String}
   * @return {@link WebhookTag}
   */
  public WebhookTag userId(final String tagUserId) {
    this.userId = tagUserId;
    return this;
  }

  /**
   * Get Tag Value.
   * 
   * @return {@link String}
   */
  public String value() {
    return this.value;
  }

  /**
   * Set Tag Value.
   * 
   * @param tagValue {@link String}
   * @return {@link WebhookTag}
   */
  public WebhookTag value(final String tagValue) {
    this.value = tagValue;
    return this;
  }
}
