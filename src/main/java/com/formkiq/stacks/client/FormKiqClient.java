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
package com.formkiq.stacks.client;

import java.io.IOException;
import java.net.http.HttpResponse;
import com.formkiq.stacks.client.models.AddDocumentResponse;
import com.formkiq.stacks.client.models.AddPresetResponse;
import com.formkiq.stacks.client.models.AddWebhookResponse;
import com.formkiq.stacks.client.models.Document;
import com.formkiq.stacks.client.models.DocumentTag;
import com.formkiq.stacks.client.models.DocumentTags;
import com.formkiq.stacks.client.models.DocumentUrl;
import com.formkiq.stacks.client.models.DocumentVersions;
import com.formkiq.stacks.client.models.Documents;
import com.formkiq.stacks.client.models.PresetTags;
import com.formkiq.stacks.client.models.Presets;
import com.formkiq.stacks.client.models.Sites;
import com.formkiq.stacks.client.models.UpdateDocumentResponse;
import com.formkiq.stacks.client.models.Version;
import com.formkiq.stacks.client.models.WebhookTags;
import com.formkiq.stacks.client.models.Webhooks;
import com.formkiq.stacks.client.requests.AddDocumentRequest;
import com.formkiq.stacks.client.requests.AddDocumentTagRequest;
import com.formkiq.stacks.client.requests.AddPresetRequest;
import com.formkiq.stacks.client.requests.AddWebhookRequest;
import com.formkiq.stacks.client.requests.AddWebhookTagRequest;
import com.formkiq.stacks.client.requests.DeleteDocumentRequest;
import com.formkiq.stacks.client.requests.DeleteDocumentTagRequest;
import com.formkiq.stacks.client.requests.DeletePresetRequest;
import com.formkiq.stacks.client.requests.DeletePresetTagRequest;
import com.formkiq.stacks.client.requests.DeleteWebhookRequest;
import com.formkiq.stacks.client.requests.GetDocumentContentUrlRequest;
import com.formkiq.stacks.client.requests.GetDocumentRequest;
import com.formkiq.stacks.client.requests.GetDocumentTagsKeyRequest;
import com.formkiq.stacks.client.requests.GetDocumentTagsRequest;
import com.formkiq.stacks.client.requests.GetDocumentUploadRequest;
import com.formkiq.stacks.client.requests.GetDocumentVersionsRequest;
import com.formkiq.stacks.client.requests.GetDocumentsRequest;
import com.formkiq.stacks.client.requests.GetPresetTagsRequest;
import com.formkiq.stacks.client.requests.GetPresetsRequest;
import com.formkiq.stacks.client.requests.GetWebhookTagsRequest;
import com.formkiq.stacks.client.requests.GetWebhooksRequest;
import com.formkiq.stacks.client.requests.PresetTagRequest;
import com.formkiq.stacks.client.requests.SearchDocumentsRequest;
import com.formkiq.stacks.client.requests.UpdateDocumentRequest;
import com.formkiq.stacks.client.requests.UpdateDocumentTagKeyRequest;

/**
 * Http Client for interacting with the FormKiQ API.
 *
 */
public interface FormKiqClient {

  /**
   * POST(Add) /documents.
   * 
   * @param request {@link AddDocumentRequest}
   * @return {@link AddDocumentResponse}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  AddDocumentResponse addDocument(AddDocumentRequest request)
      throws IOException, InterruptedException;

  /**
   * POST /documents/{documentId}/tags.
   * 
   * @param request {@link AddDocumentTagRequest}
   * @return boolean
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  boolean addDocumentTag(AddDocumentTagRequest request) throws IOException, InterruptedException;

  /**
   * POST /webhooks/{webhookId}/tags.
   * 
   * @param request {@link AddDocumentTagRequest}
   * @return boolean
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  boolean addWebhookTag(AddWebhookTagRequest request) throws IOException, InterruptedException;

  /**
   * POST(Add) /presets.
   * 
   * @param request {@link AddPresetRequest}
   * @return {@link AddPresetResponse}
   * @throws IOException IOExceptions
   * @throws InterruptedException InterruptedException
   */

  AddPresetResponse addPreset(AddPresetRequest request) throws IOException, InterruptedException;

  /**
   * Post /presets/{presetId}/tags.
   * 
   * @param request {@link PresetTagRequest}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  void addPresetTags(PresetTagRequest request) throws IOException, InterruptedException;

  /**
   * POST(Add) /webhook.
   * 
   * @param request {@link AddWebhookRequest}
   * @return {@link AddWebhookResponse}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  AddWebhookResponse addWebhook(AddWebhookRequest request) throws IOException, InterruptedException;

  /**
   * DELETE /documents/{documentId}.
   * 
   * @param request {@link DeleteDocumentRequest}
   * @return boolean - Whether the document was successfully deleted
   * 
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  boolean deleteDocument(DeleteDocumentRequest request) throws IOException, InterruptedException;

  /**
   * DELETE /documents/{documentId}/tags/{tagKey}.
   * 
   * @param request {@link DeleteDocumentTagRequest}
   * @return {@link HttpResponse} {@link String}
   * 
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  boolean deleteDocumentTag(DeleteDocumentTagRequest request)
      throws IOException, InterruptedException;

  /**
   * DELETE /presets/{presetsId}.
   * 
   * @param request {@link DeletePresetRequest}
   * @return boolean - Whether delete was successfully
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  boolean deletePreset(DeletePresetRequest request) throws IOException, InterruptedException;

  /**
   * DELETE /preset/{presetId}/tags/{tagKey}.
   * 
   * @param request {@link DeletePresetTagRequest}
   * @return boolean - Whether delete was successfully
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  boolean deletePresetTag(DeletePresetTagRequest request) throws IOException, InterruptedException;

  /**
   * DELETE /documents/{webhookId}.
   * 
   * @param request {@link DeleteWebhookRequest}
   * @return boolean - Whether the document was successfully deleted
   * 
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  boolean deleteWebhook(DeleteWebhookRequest request) throws IOException, InterruptedException;

  /**
   * GET /documents/{documentId}.
   * 
   * @param request {@link GetDocumentRequest}
   * @return {@link Document}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  Document getDocument(GetDocumentRequest request) throws IOException, InterruptedException;

  /**
   * GET /documents/{documentId}/url.
   * 
   * @param request {@link GetDocumentContentUrlRequest}
   * @return {@link DocumentUrl}
   * 
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  DocumentUrl getDocumentContentUrl(GetDocumentContentUrlRequest request)
      throws IOException, InterruptedException;

  /**
   * GET /documents.
   * 
   * @param request {@link GetDocumentsRequest}
   * 
   * @return {@link Documents}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  Documents getDocuments(GetDocumentsRequest request) throws IOException, InterruptedException;

  /**
   * GET /documents/{documentId}/tags/{tagKey}.
   * 
   * @param request {@link GetDocumentTagsKeyRequest}
   * @return {@link DocumentTag}
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  DocumentTag getDocumentTag(GetDocumentTagsKeyRequest request)
      throws IOException, InterruptedException;

  /**
   * GET /documents/{documentId}/tags.
   * 
   * @param request {@link GetDocumentTagsRequest}
   * @return {@link DocumentTags}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  DocumentTags getDocumentTags(GetDocumentTagsRequest request)
      throws IOException, InterruptedException;

  /**
   * GET /documents/upload or /documents/{documentId}/upload.
   * 
   * @param request {@link GetDocumentUploadRequest}
   * @return {@link DocumentUrl}
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  DocumentUrl getDocumentUpload(GetDocumentUploadRequest request)
      throws IOException, InterruptedException;

  /**
   * GET /documents/{documentId}/versions.
   * 
   * @param request {@link GetDocumentVersionsRequest}
   * @return {@link DocumentVersions}
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  DocumentVersions getDocumentVersions(GetDocumentVersionsRequest request)
      throws IOException, InterruptedException;

  /**
   * Get /presets.
   * 
   * @param request {@link GetPresetsRequest}
   * @return {@link Presets}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  Presets getPresets(GetPresetsRequest request) throws IOException, InterruptedException;

  /**
   * Get /presets/{presetId}/tags.
   * 
   * @param request {@link GetPresetTagsRequest}
   * @return {@link PresetTags}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  PresetTags getPresetTags(GetPresetTagsRequest request) throws IOException, InterruptedException;

  /**
   * GET /sites.
   * 
   * @return {@link HttpResponse}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  Sites getSites() throws IOException, InterruptedException;

  /**
   * GET /version.
   * 
   * @return {@link Version}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  Version getVersion() throws IOException, InterruptedException;

  /**
   * Get /webhooks.
   * 
   * @param request {@link GetWebhooksRequest}
   * @return {@link Webhooks}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  Webhooks getWebhooks(GetWebhooksRequest request) throws IOException, InterruptedException;

  /**
   * Get /webhooks/${webhookId}/tags.
   * 
   * @param request {@link GetWebhookTagsRequest}
   * @return {@link WebhookTags}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  WebhookTags getWebhookTags(GetWebhookTagsRequest request)
      throws IOException, InterruptedException;

  /**
   * POST /search.
   * 
   * @param request {@link SearchDocumentsRequest}
   * @return {@link Documents}
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  Documents search(SearchDocumentsRequest request) throws IOException, InterruptedException;

  /**
   * PATCH(Update) /documents/{documentId}.
   * 
   * @param request {@link UpdateDocumentRequest}
   * @return {@link UpdateDocumentResponse}
   * @throws IOException IOException
   * @throws InterruptedException InterruptedException
   */
  UpdateDocumentResponse updateDocument(UpdateDocumentRequest request)
      throws IOException, InterruptedException;

  /**
   * PUT /documents/{documentId}/tags/{tagKey}.
   * 
   * @param request {@link UpdateDocumentTagKeyRequest}
   * @return boolean
   * 
   * @throws InterruptedException InterruptedException
   * @throws IOException IOException
   */
  boolean updateDocumentTag(UpdateDocumentTagKeyRequest request)
      throws IOException, InterruptedException;
}
