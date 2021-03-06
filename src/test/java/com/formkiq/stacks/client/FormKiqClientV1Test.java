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

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.IOUtils.resourceToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import com.formkiq.stacks.client.models.AddDocument;
import com.formkiq.stacks.client.models.AddDocumentResponse;
import com.formkiq.stacks.client.models.AddPresetResponse;
import com.formkiq.stacks.client.models.AddWebhookResponse;
import com.formkiq.stacks.client.models.Document;
import com.formkiq.stacks.client.models.DocumentContent;
import com.formkiq.stacks.client.models.DocumentTag;
import com.formkiq.stacks.client.models.DocumentTags;
import com.formkiq.stacks.client.models.DocumentUrl;
import com.formkiq.stacks.client.models.DocumentVersions;
import com.formkiq.stacks.client.models.Documents;
import com.formkiq.stacks.client.models.PresetTagBody;
import com.formkiq.stacks.client.models.PresetTags;
import com.formkiq.stacks.client.models.Presets;
import com.formkiq.stacks.client.models.PresetsBody;
import com.formkiq.stacks.client.models.Sites;
import com.formkiq.stacks.client.models.UpdateDocument;
import com.formkiq.stacks.client.models.UpdateDocumentResponse;
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
import com.formkiq.stacks.client.requests.DocumentFormatSearchRequest;
import com.formkiq.stacks.client.requests.GetDocumentContentRequest;
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
import com.formkiq.stacks.client.requests.OptionsDocumentContentRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentContentUrlRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentFormatRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentTagsKeyRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentTagsRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentUploadRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentVersionsRequest;
import com.formkiq.stacks.client.requests.OptionsPresetRequest;
import com.formkiq.stacks.client.requests.OptionsPresetTagsRequest;
import com.formkiq.stacks.client.requests.OptionsWebhookRequest;
import com.formkiq.stacks.client.requests.OptionsWebhookTagsRequest;
import com.formkiq.stacks.client.requests.PresetTagRequest;
import com.formkiq.stacks.client.requests.SearchDocumentsRequest;
import com.formkiq.stacks.client.requests.UpdateDocumentRequest;
import com.formkiq.stacks.client.requests.UpdateDocumentTagKeyRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;

/**
 * 
 * Unit Test for {@link FormKiqClient}.
 *
 */
public class FormKiqClientV1Test {

  /** Date Format. */
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
  /** Http Status OK. */
  private static final int HTTP_STATUS_OK = 200;
  /** Http Status Created. */
  private static final int HTTP_STATUS_CREATED = 201;
  /** Random Version Identifier. */
  private static String versionId = UUID.randomUUID().toString();
  /** Random Site Identifier. */
  private static String siteId = UUID.randomUUID().toString();
  /** Random Document Identifier. */
  private static String documentId = UUID.randomUUID().toString();
  /** Port to run Test server. */
  private static final int PORT = 8080;
  /** Test server URL. */
  private static final String URL = "http://localhost:" + PORT;
  /** {@link DateFormat}. */
  private static DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
  /** {@link Gson}. */
  private static Gson gson;
  /** {@link ClientAndServer}. */
  private static ClientAndServer mockServer;
  /** Year 2020. */
  private static final int YEAR = 2020;
  /** {@link Date}. */
  private static Date date =
      Date.from(LocalDate.of(YEAR, 2, 1).atStartOfDay(ZoneId.systemDefault()).toInstant());

  /**
   * Add Url to Mock server.
   * 
   * @param method {@link String}
   * @param path {@link String}
   * @param file {@link String}
   * @throws IOException IOException
   */
  private static void add(final String method, final String path, final String file)
      throws IOException {
    mockServer.when(request().withMethod(method).withPath(path))
        .respond(org.mockserver.model.HttpResponse.response(resourceToString(file, UTF_8)));
  }

  /**
   * Add /presets urls.
   * 
   * @throws IOException IOException
   */
  private static void addPresets() throws IOException {
    add("get", "/presets", "/get_presets.json");
    add("post", "/presets", "/id.json");
    add("options", "/presets", "/id.json");
    add("options", "/presets/" + documentId, "/id.json");
    add("delete", "/presets/" + documentId, "/id.json");
    add("delete", "/presets/" + documentId, "/id.json");
    add("get", "/presets/" + documentId + "/tags", "/get_presets_tags.json");
    add("patch", "/presets/" + documentId + "/tags", "/id.json");
    mockServer.when(request().withMethod("post").withPath("/presets/" + documentId + "/tags"))
        .respond(org.mockserver.model.HttpResponse.response(resourceToString("/id.json", UTF_8))
            .withStatusCode(Integer.valueOf(HTTP_STATUS_CREATED)));
    add("options", "/presets/" + documentId + "/tags", "/id.json");
    add("options", "/presets/" + documentId + "/tags/first+name", "/id.json");
    add("delete", "/presets/" + documentId + "/tags/first+name", "/id.json");
  }

  /**
   * Add /webhooks urls.
   * 
   * @throws IOException IOException
   */
  private static void addWebhooks() throws IOException {
    mockServer.when(request().withMethod("post").withPath("/webhooks/" + documentId + "/tags"))
        .respond(
            org.mockserver.model.HttpResponse.response(resourceToString("/documentsId.json", UTF_8))
                .withStatusCode(Integer.valueOf(HTTP_STATUS_CREATED)));

    add("get", "/webhooks", "/get_webhooks.json");
    add("post", "/webhooks", "/id.json");
    add("options", "/webhooks", "/id.json");
    add("options", "/webhooks/" + documentId, "/id.json");
    add("delete", "/webhooks/" + documentId, "/id.json");

    add("options", "/webhooks/" + documentId + "/tags", "/documentsId.json");
    add("get", "/webhooks/" + documentId + "/tags", "/get_webhooks_tags.json");
  }

  /**
   * Initialize BeforeClass.
   * 
   * @throws IOException IOException
   */
  @BeforeClass
  public static void startServer() throws IOException {

    df.setTimeZone(TimeZone.getTimeZone("GMT"));
    mockServer = startClientAndServer(Integer.valueOf(PORT));

    gson = new GsonBuilder().disableHtmlEscaping().setDateFormat(DATE_FORMAT).create();

    add("get", "/version", "/get_version.json");
    add("options", "/version", "/id.json");

    add("get", "/sites", "/get_sites.json");
    add("options", "/sites", "/id.json");

    addWebhooks();
    addPresets();

    add("get", "/documents", "/get_documents.json");
    add("post", "/documents", "/documentsId.json");
    add("post", "/public/documents", "/documentsId.json");
    add("options", "/documents", "/documentsId.json");
    add("patch", "/documents/" + documentId, "/documentsId.json");
    add("get", "/documents/" + documentId, "/get_document.json");
    add("delete", "/documents/" + documentId, "/documentsId.json");
    add("get", "/documents/" + documentId + "/tags", "/get_documents_tags.json");
    add("get", "/documents/" + documentId + "/versions", "/get_documents_versions.json");
    add("options", "/documents/" + documentId + "/versions", "/get_documents_versions.json");
    mockServer.when(request().withMethod("post").withPath("/documents/" + documentId + "/tags"))
        .respond(
            org.mockserver.model.HttpResponse.response(resourceToString("/documentsId.json", UTF_8))
                .withStatusCode(Integer.valueOf(HTTP_STATUS_CREATED)));
    add("options", "/documents/" + documentId + "/tags", "/documentsId.json");
    add("options", "/documents/" + documentId + "/content", "/documentsId.json");
    add("options", "/documents/" + documentId, "/documentsId.json");
    add("get", "/documents/" + documentId + "/tags/category", "/get_documents_tag.json");
    add("put", "/documents/" + documentId + "/tags/category", "/documentsId.json");
    add("options", "/documents/" + documentId + "/tags/category", "/documentsId.json");
    add("delete", "/documents/" + documentId + "/tags/category", "/documentsId.json");
    add("get", "/documents/" + documentId + "/url", "/get_documents_url.json");
    add("options", "/documents/" + documentId + "/url", "/documentsId.json");
    add("get", "/documents/upload", "/get_documents_url.json");
    add("options", "/documents/upload", "/documentsId.json");
    add("get", "/documents/" + documentId + "/upload", "/get_documents_url.json");
    add("options", "/documents/" + documentId + "/upload", "/documentsId.json");
    add("post", "/documents/" + documentId + "/formats", "/get_documents_url.json");
    add("options", "/documents/" + documentId + "/formats", "/documentsId.json");
    add("post", "/search", "/search.json");
    add("options", "/search", "/search.json");
    add("get", "/documents/" + documentId + "/content", "/get_documents_content.json");
  }

  /**
   * After Class.
   */
  @AfterClass
  public static void stopServer() {
    mockServer.stop();
  }

  /** {@link FormKiqClientConnection}. */
  private FormKiqClientConnection connection = new FormKiqClientConnection(URL)
      .region(Region.US_EAST_1).credentials(AwsBasicCredentials.create("123", "444"))
      .header("origin", Arrays.asList("http://localhost")).cognitoIdToken("AAAA");

  /** {@link FormKiqClientConnection}. */
  private FormKiqClientConnection connectionEndSlash = new FormKiqClientConnection(URL + "/")
      .region(Region.US_EAST_1).credentials(AwsBasicCredentials.create("123", "444"))
      .header("origin", Arrays.asList("http://localhost")).cognitoIdToken("AAAA");

  /** {@link FormKiqClient}. */
  private FormKiqClientV1 client = new FormKiqClientV1(this.connection);

  /** {@link FormKiqClient}. */
  private FormKiqClientV1 clientEndSlash = new FormKiqClientV1(this.connectionEndSlash);

  /**
   * Test POST /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocument01() throws Exception {
    AddDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), AddDocument.class);
    AddDocumentRequest req = new AddDocumentRequest().document(post).siteId(siteId);
    AddDocumentResponse response = this.client.addDocument(req);

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc", response.documentId());
  }

  /**
   * Test POST /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocument02() throws Exception {
    AddDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), AddDocument.class);
    AddDocumentRequest req = new AddDocumentRequest().document(post).siteId(siteId);
    AddDocumentResponse response = this.clientEndSlash.addDocument(req);

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc", response.documentId());
  }

  /**
   * Test POST /documents. Missing Content
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocument03() throws Exception {
    AddDocumentRequest req = new AddDocumentRequest();

    try {
      this.client.addDocument(req);
    } catch (NullPointerException e) {
      assertEquals("Document is required.", e.getMessage());
    }
  }

  /**
   * Test POST /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocumentAsHttpResponse01() throws Exception {
    AddDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), AddDocument.class);
    AddDocumentRequest req = new AddDocumentRequest().document(post).siteId(siteId);
    HttpResponse<String> response = this.client.addDocumentAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents?siteId=" + siteId, response.request().uri().toString());
    assertEquals("POST", response.request().method());

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc",
        gson.fromJson(response.body(), Map.class).get("documentId").toString());
    HttpRequest request = response.request();
    assertEquals("[plan/text]", request.headers().map().get("Content-Type").toString());
  }

  /**
   * Test POST /documents without ContentType.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocumentAsHttpResponse02() throws Exception {
    AddDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), AddDocument.class);
    post.contentType(null);
    AddDocumentRequest req = new AddDocumentRequest().document(post);
    HttpResponse<String> response = this.client.addDocumentAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents", response.request().uri().toString());
    assertEquals("POST", response.request().method());

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc",
        gson.fromJson(response.body(), Map.class).get("documentId").toString());
    HttpRequest request = response.request();
    assertNull(request.headers().map().get("Content-Type"));
  }

  /**
   * Test POST /public/documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocumentAsHttpResponse03() throws Exception {
    AddDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), AddDocument.class);
    AddDocumentRequest req =
        new AddDocumentRequest().enablePublicEndpoint(true).document(post).siteId(siteId);
    HttpResponse<String> response = this.client.addDocumentAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/public/documents?siteId=" + siteId, response.request().uri().toString());
    assertEquals("POST", response.request().method());

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc",
        gson.fromJson(response.body(), Map.class).get("documentId").toString());
    HttpRequest request = response.request();
    assertEquals("[plan/text]", request.headers().map().get("Content-Type").toString());
  }

  /**
   * Test POST /documents/{documentId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocumentTag01() throws Exception {
    AddDocumentTagRequest req = new AddDocumentTagRequest().siteId(siteId).documentId(documentId)
        .tagKey("category").tagValue("person");
    assertTrue(this.client.addDocumentTag(req));
  }

  /**
   * Test POST /documents/{documentId}/tags. Missing content.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocumentTag02() throws Exception {
    AddDocumentTagRequest req = new AddDocumentTagRequest();

    try {
      this.client.addDocumentTag(req);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test POST /documents/{documentId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocumentTagAsHttpResponse() throws Exception {
    AddDocumentTagRequest req = new AddDocumentTagRequest().siteId(siteId).documentId(documentId)
        .tagKey("category").tagValue("person");
    HttpResponse<String> response = this.client.addDocumentTagAsHttpResponse(req);
    assertEquals(HTTP_STATUS_CREATED, response.statusCode());
    assertEquals("POST", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/tags?siteId=" + siteId,
        response.request().uri().toString());
  }

  /**
   * Test POST /presets.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddPreset01() throws Exception {
    PresetsBody post =
        gson.fromJson(resourceToString("/post_presets.json", UTF_8), PresetsBody.class);
    AddPresetRequest req = new AddPresetRequest().body(post).siteId(siteId);

    AddPresetResponse response = this.client.addPreset(req);
    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc", response.id());

    HttpResponse<String> httpresponse = this.client.addPresetAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, httpresponse.statusCode());
    assertEquals(URL + "/presets?siteId=" + siteId, httpresponse.request().uri().toString());
    assertEquals("POST", httpresponse.request().method());

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc",
        gson.fromJson(httpresponse.body(), Map.class).get("id").toString());
  }

  /**
   * Test POST /presets/{presetId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddPresetTags01() throws Exception {
    PresetTagRequest req = new PresetTagRequest().siteId(siteId).presetId(documentId)
        .body(new PresetTagBody().key("First Name"));
    this.client.addPresetTags(req);
  }

  /**
   * Test POST /presets/{presetId}/tags. Missing content.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddPresetTags02() throws Exception {
    PresetTagRequest req = new PresetTagRequest();

    try {
      this.client.addPresetTags(req);
    } catch (NullPointerException e) {
      assertEquals("PresetId is required.", e.getMessage());
    }
  }

  /**
   * Test POST /presets/{documentId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddPresetTagsAsHttpResponse() throws Exception {
    PresetTagRequest req = new PresetTagRequest().siteId(siteId).presetId(documentId)
        .body(new PresetTagBody().key("First Name"));
    HttpResponse<String> response = this.client.addPresetTagsAsHttpResponse(req);
    assertEquals(HTTP_STATUS_CREATED, response.statusCode());
    assertEquals("POST", response.request().method());
    assertEquals(URL + "/presets/" + documentId + "/tags?siteId=" + siteId,
        response.request().uri().toString());
  }

  /**
   * Test POST /webhooks.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddWebhookAsHttpResponse01() throws Exception {
    AddWebhookRequest req = new AddWebhookRequest().name("test").siteId(siteId);
    HttpResponse<String> response = this.client.addWebhookAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/webhooks?siteId=" + siteId, response.request().uri().toString());
    assertEquals("POST", response.request().method());
    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc",
        gson.fromJson(response.body(), Map.class).get("id").toString());
  }

  /**
   * Test POST /webhooks.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddWebhooks01() throws Exception {
    AddWebhookRequest req = new AddWebhookRequest().name("test").siteId(siteId);
    AddWebhookResponse response = this.client.addWebhook(req);

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc", response.id());
    assertEquals("default", response.siteId());
  }

  /**
   * Test POST /webhooks/{webhookId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddWebhookTag01() throws Exception {
    AddWebhookTagRequest req = new AddWebhookTagRequest().siteId(siteId).webhookId(documentId)
        .tagKey("category").tagValue("person");
    assertTrue(this.client.addWebhookTag(req));
  }

  /**
   * Test POST /webhooks/{webhookId}/tags. Missing content.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddWebhookTag02() throws Exception {
    AddWebhookTagRequest req = new AddWebhookTagRequest();

    try {
      this.client.addWebhookTag(req);
    } catch (NullPointerException e) {
      assertEquals("WebhookId is required.", e.getMessage());
    }
  }

  /**
   * Test POST /webhooks/{webhookId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddWebhookTagAsHttpResponse() throws Exception {
    AddWebhookTagRequest req = new AddWebhookTagRequest().siteId(siteId).webhookId(documentId)
        .tagKey("category").tagValue("person");
    HttpResponse<String> response = this.client.addWebhookTagAsHttpResponse(req);
    assertEquals(HTTP_STATUS_CREATED, response.statusCode());
    assertEquals("POST", response.request().method());
    assertEquals(URL + "/webhooks/" + documentId + "/tags?siteId=" + siteId,
        response.request().uri().toString());
  }

  /**
   * Test DELETE /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocument01() throws Exception {
    DeleteDocumentRequest request =
        new DeleteDocumentRequest().documentId(documentId).siteId(siteId);
    assertTrue(this.client.deleteDocument(request));
  }

  /**
   * Test DELETE /documents/{documentid}. Missing Data.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocument02() throws Exception {
    DeleteDocumentRequest request = new DeleteDocumentRequest();
    try {
      this.client.deleteDocument(request);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test DELETE /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocumentAsHttpResponse() throws Exception {
    DeleteDocumentRequest request =
        new DeleteDocumentRequest().documentId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.deleteDocumentAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("DELETE", response.request().method());
  }

  /**
   * Test DELETE /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocumentTag01() throws Exception {
    DeleteDocumentTagRequest request =
        new DeleteDocumentTagRequest().documentId(documentId).tagKey("category").siteId(siteId);
    assertTrue(this.client.deleteDocumentTag(request));
  }

  /**
   * Test DELETE /documents/{documentId}/tags/{tagKey}. Missing Data.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocumentTag02() throws Exception {
    DeleteDocumentTagRequest request = new DeleteDocumentTagRequest();
    try {
      this.client.deleteDocumentTag(request);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test DELETE /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocumentTagAsHttpResponse() throws Exception {
    DeleteDocumentTagRequest request =
        new DeleteDocumentTagRequest().documentId(documentId).tagKey("category").siteId(siteId);
    HttpResponse<String> response = this.client.deleteDocumentTagAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("DELETE", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/tags/category?siteId=" + siteId,
        response.request().uri().toString());
  }

  /**
   * Test DELETE /presets/{presetid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeletePreset01() throws Exception {
    DeletePresetRequest request = new DeletePresetRequest().presetId(documentId).siteId(siteId);
    assertTrue(this.client.deletePreset(request));
  }

  /**
   * Test DELETE /presets/{presetid}. Missing Data.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeletePreset02() throws Exception {
    DeletePresetRequest request = new DeletePresetRequest();
    try {
      this.client.deletePreset(request);
    } catch (NullPointerException e) {
      assertEquals("PresetId is required.", e.getMessage());
    }
  }

  /**
   * Test DELETE /presets/{presetid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeletePresetAsHttpResponse() throws Exception {
    DeletePresetRequest request = new DeletePresetRequest().presetId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.deletePresetAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/presets/" + documentId + "?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("DELETE", response.request().method());
  }

  /**
   * Test DELETE /presets/{presetId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeletePresetTag() throws Exception {
    DeletePresetTagRequest request =
        new DeletePresetTagRequest().presetId(documentId).siteId(siteId).tag("first name");
    assertTrue(this.client.deletePresetTag(request));
  }

  /**
   * Test DELETE /presets/{presetId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeletePresetTagAsHttpResponse() throws Exception {
    DeletePresetTagRequest request =
        new DeletePresetTagRequest().presetId(documentId).siteId(siteId).tag("first name");
    HttpResponse<String> response = this.client.deletePresetTagAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/presets/" + documentId + "/tags/first+name?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("DELETE", response.request().method());
  }

  /**
   * Test DELETE /webhooks/{webhookId}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteWebhook01() throws Exception {
    DeleteWebhookRequest request = new DeleteWebhookRequest().webhookId(documentId).siteId(siteId);
    assertTrue(this.client.deleteWebhook(request));
  }

  /**
   * Test GET /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocument01() throws Exception {
    GetDocumentRequest req = new GetDocumentRequest().documentId(documentId).siteId(siteId);
    Document doc = this.client.getDocument(req);
    assertEquals("1000", "" + doc.contentLength());
    assertEquals("text/plain", doc.contentType());
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", doc.documentId());
    assertEquals("efjsdkfhds", doc.checksum());
    assertEquals("2020/05/05 17:31:06", df.format(doc.insertedDate()));
    assertEquals("sample/test.txt", doc.path());
    assertEquals("6981181a-bbb1-4228-a65d-6dc947f036ac@formkiq.com", doc.userId());
  }

  /**
   * Test GET /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocument02() throws Exception {
    GetDocumentRequest req = new GetDocumentRequest();
    try {
      this.client.getDocument(req);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test GET /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentAsHttpResponse() throws Exception {
    GetDocumentRequest req = new GetDocumentRequest().documentId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.getDocumentAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("GET", response.request().method());

    Document doc = gson.fromJson(response.body(), Document.class);
    assertEquals("1000", "" + doc.contentLength());
    assertEquals("text/plain", doc.contentType());
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", doc.documentId());
    assertEquals("efjsdkfhds", doc.checksum());
    assertEquals("2020/05/05 17:31:06", df.format(doc.insertedDate()));
    assertEquals("sample/test.txt", doc.path());
    assertEquals("6981181a-bbb1-4228-a65d-6dc947f036ac@formkiq.com", doc.userId());
  }

  /**
   * Test GET /documents/{documentId}/content.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentContent() throws Exception {
    GetDocumentContentRequest request =
        new GetDocumentContentRequest().documentId(documentId).versionId("100").siteId("123");
    DocumentContent response = this.client.getDocumentContent(request);
    assertEquals("this is a test", response.content());
    assertEquals("text/plain", response.contentType());
    assertEquals("http://www.google.com", response.contentUrl());
    assertFalse("", response.isBase64());
  }

  /**
   * Test GET /documents/{documentId}/content.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentContentAsHttpResponse() throws Exception {
    GetDocumentContentRequest request =
        new GetDocumentContentRequest().documentId(documentId).versionId("101").siteId("123");
    HttpResponse<String> response = this.client.getDocumentContentAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("GET", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/content?versionId=101&siteId=123",
        response.request().uri().toString());
  }

  /**
   * Test GET /documents/{documentId}/url.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentContentUrl01() throws Exception {
    GetDocumentContentUrlRequest req =
        new GetDocumentContentUrlRequest().documentId(documentId).duration(1).siteId(siteId);
    DocumentUrl url = this.client.getDocumentContentUrl(req);
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", url.documentId());
    assertEquals("https://www.google.com", url.url());
  }

  /**
   * Test GET /documents/{documentId}/url. No Data.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentContentUrl02() throws Exception {
    GetDocumentContentUrlRequest req = new GetDocumentContentUrlRequest();
    try {
      this.client.getDocumentContentUrl(req);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test GET /documents/{documentId}/url.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentContentUrlAsHttpResponse() throws Exception {
    GetDocumentContentUrlRequest req = new GetDocumentContentUrlRequest().documentId(documentId)
        .duration(1).siteId(siteId).versionId(versionId);
    HttpResponse<String> response = this.client.getDocumentContentUrlAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "/url?duration=1&versionId=" + versionId
        + "&siteId=" + siteId, response.request().uri().toString());
    assertEquals("GET", response.request().method());

    DocumentUrl url = gson.fromJson(response.body(), DocumentUrl.class);
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", url.documentId());
    assertEquals("https://www.google.com", url.url());
  }

  /**
   * Test GET /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocuments() throws Exception {
    GetDocumentsRequest request = new GetDocumentsRequest().date(date).limit(1).next("nnnn")
        .previous("ppp").siteId(siteId).tz("0500");
    Documents docs = this.client.getDocuments(request);
    assertEquals("123", docs.next());
    assertEquals("555", docs.previous());
    assertEquals(1, docs.documents().size());
    assertEquals("1000", "" + docs.documents().get(0).contentLength());
    assertEquals("text/plain", docs.documents().get(0).contentType());
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", docs.documents().get(0).documentId());
    assertEquals("efjsdkfhds", docs.documents().get(0).checksum());
    assertEquals("2020/05/05 17:31:06", df.format(docs.documents().get(0).insertedDate()));
    assertEquals("sample/test.txt", docs.documents().get(0).path());
    assertEquals("6981181a-bbb1-4228-a65d-6dc947f036ac@formkiq.com",
        docs.documents().get(0).userId());
  }

  /**
   * Test GET /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentsAsHttpResponse() throws Exception {
    GetDocumentsRequest request = new GetDocumentsRequest().date(date).limit(1).next("nnnn")
        .previous("ppp").siteId(siteId).tz("0500");
    HttpResponse<String> response = this.client.getDocumentsAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(
        URL + "/documents?next=nnnn&date=2020-02-01&previous=ppp&tz=0500&limit=1&siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("GET", response.request().method());

    Documents docs = gson.fromJson(response.body(), Documents.class);
    assertEquals("123", docs.next());
    assertEquals("555", docs.previous());
    assertEquals(1, docs.documents().size());
    assertEquals("1000", "" + docs.documents().get(0).contentLength());
    assertEquals("text/plain", docs.documents().get(0).contentType());
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", docs.documents().get(0).documentId());
    assertEquals("efjsdkfhds", docs.documents().get(0).checksum());
    assertEquals("2020/05/05 17:31:06", df.format(docs.documents().get(0).insertedDate()));
    assertEquals("sample/test.txt", docs.documents().get(0).path());
    assertEquals("6981181a-bbb1-4228-a65d-6dc947f036ac@formkiq.com",
        docs.documents().get(0).userId());
  }

  /**
   * Test GET /documents/{documentid}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentsTags01() throws Exception {
    GetDocumentTagsRequest req = new GetDocumentTagsRequest().documentId(documentId).limit(1)
        .next("nn").previous("pp").siteId(siteId);
    DocumentTags tags = this.client.getDocumentTags(req);
    assertEquals("123", tags.next());
    assertEquals("555", tags.previous());
    assertEquals(1, tags.tags().size());
    assertEquals("999", tags.tags().get(0).documentId());
    assertEquals("2020/05/05 18:11:36", df.format(tags.tags().get(0).insertedDate()));
    assertEquals("category", tags.tags().get(0).key());
    assertEquals("userdefined", tags.tags().get(0).type());
    assertEquals("jsmith", tags.tags().get(0).userId());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", tags.tags().get(0).value());
  }

  /**
   * Test GET /documents/{documentid}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentsTags02() throws Exception {
    GetDocumentTagsRequest req = new GetDocumentTagsRequest();
    try {
      this.client.getDocumentTags(req);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test GET /documents/{documentid}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentsTagsAsHttpResponse() throws Exception {
    GetDocumentTagsRequest req = new GetDocumentTagsRequest().documentId(documentId).limit(1)
        .next("nn").previous("pp").siteId(siteId);
    HttpResponse<String> response = this.client.getDocumentTagsAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(
        URL + "/documents/" + documentId + "/tags?next=nn&previous=pp&limit=1&siteId=" + siteId,
        response.request().uri().toString());

    DocumentTags tags = gson.fromJson(response.body(), DocumentTags.class);
    assertEquals("123", tags.next());
    assertEquals("555", tags.previous());
    assertEquals(1, tags.tags().size());
    assertEquals("999", tags.tags().get(0).documentId());
    assertEquals("2020/05/05 18:11:36", df.format(tags.tags().get(0).insertedDate()));
    assertEquals("category", tags.tags().get(0).key());
    assertEquals("userdefined", tags.tags().get(0).type());
    assertEquals("jsmith", tags.tags().get(0).userId());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", tags.tags().get(0).value());
  }

  /**
   * Test GET /documents/{documentid}/versions.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentsVersions01() throws Exception {
    GetDocumentVersionsRequest req = new GetDocumentVersionsRequest().documentId(documentId)
        .next("nn").tz("-0600").siteId(siteId);
    DocumentVersions versions = this.client.getDocumentVersions(req);
    assertEquals("123", versions.next());
    assertEquals(1, versions.versions().size());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", versions.versions().get(0).versionId());
    assertEquals("2020/05/05 18:11:36", df.format(versions.versions().get(0).lastModifiedDate()));
  }

  /**
   * Test GET /documents/{documentid}/versions.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentsVersions02() throws Exception {
    GetDocumentVersionsRequest req = new GetDocumentVersionsRequest();
    try {
      this.client.getDocumentVersions(req);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test GET /documents/{documentid}/versions.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentsVersionsAsHttpResponse() throws Exception {
    GetDocumentVersionsRequest req = new GetDocumentVersionsRequest().documentId(documentId)
        .next("nn").tz("-0600").siteId(siteId);
    HttpResponse<String> response = this.client.getDocumentVersionsAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "/versions?next=nn&tz=-0600&siteId=" + siteId,
        response.request().uri().toString());

    DocumentVersions versions = gson.fromJson(response.body(), DocumentVersions.class);
    assertEquals("123", versions.next());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", versions.versions().get(0).versionId());
    assertEquals("2020/05/05 18:11:36", df.format(versions.versions().get(0).lastModifiedDate()));
  }

  /**
   * Test GET /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentTag01() throws Exception {
    GetDocumentTagsKeyRequest request =
        new GetDocumentTagsKeyRequest().documentId(documentId).tagKey("category").siteId(siteId);
    DocumentTag tag = this.client.getDocumentTag(request);
    assertEquals("2020/05/05 18:11:36", df.format(tag.insertedDate()));
    assertEquals("category", tag.key());
    assertEquals("userdefined", tag.type());
    assertEquals("jsmith", tag.userId());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", tag.value());
    assertEquals("999", tag.documentId());
  }

  /**
   * Test GET /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentTag02() throws Exception {
    GetDocumentTagsKeyRequest request = new GetDocumentTagsKeyRequest();
    try {
      this.client.getDocumentTag(request);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test GET /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentTagAsHttpResponse() throws Exception {
    GetDocumentTagsKeyRequest request =
        new GetDocumentTagsKeyRequest().documentId(documentId).tagKey("category").siteId(siteId);
    HttpResponse<String> response = this.client.getDocumentTagAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("GET", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/tags/category?siteId=" + siteId,
        response.request().uri().toString());

    DocumentTag tag = gson.fromJson(response.body(), DocumentTag.class);
    assertEquals("2020/05/05 18:11:36", df.format(tag.insertedDate()));
    assertEquals("category", tag.key());
    assertEquals("userdefined", tag.type());
    assertEquals("jsmith", tag.userId());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", tag.value());
    assertEquals("999", tag.documentId());
  }

  /**
   * Test GET /documents/upload.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentUpload() throws Exception {
    DocumentUrl url = this.client.getDocumentUpload(new GetDocumentUploadRequest().contentLength(1)
        .documentId(null).duration(2).path("test.txt"));
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", url.documentId());
    assertEquals("https://www.google.com", url.url());
  }

  /**
   * Test GET /documents/upload.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentUploadAsHttpResponse01() throws Exception {
    HttpResponse<String> response =
        this.client.getDocumentUploadAsHttpResponse(new GetDocumentUploadRequest().contentLength(1)
            .documentId(null).duration(2).path("test.txt"));
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/upload?duration=2&path=test.txt&contentLength=1",
        response.request().uri().toString());
    assertEquals("GET", response.request().method());

    DocumentUrl url = gson.fromJson(response.body(), DocumentUrl.class);
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", url.documentId());
    assertEquals("https://www.google.com", url.url());
  }

  /**
   * Test GET /documents/{documentId}/upload.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentUploadAsHttpResponse02() throws Exception {
    GetDocumentUploadRequest req = new GetDocumentUploadRequest().documentId(documentId)
        .siteId(siteId).contentLength(1).duration(2).path("test.txt");
    HttpResponse<String> response = this.client.getDocumentUploadAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("GET", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/upload?duration=2&path=test.txt&siteId="
        + siteId + "&contentLength=1", response.request().uri().toString());

    DocumentUrl url = gson.fromJson(response.body(), DocumentUrl.class);
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", url.documentId());
    assertEquals("https://www.google.com", url.url());
  }

  /**
   * Test GET /presets.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetPresets() throws Exception {
    GetPresetsRequest request =
        new GetPresetsRequest().limit(1).next("nnnn").previous("ppp").siteId(siteId);
    Presets p = this.client.getPresets(request);
    verifyGetPresets(p);
  }

  /**
   * Test GET /presets.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetPresetsAsHttpResponse() throws Exception {
    GetPresetsRequest request =
        new GetPresetsRequest().limit(1).next("nnnn").previous("ppp").siteId(siteId);
    HttpResponse<String> response = this.client.getPresetsAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/presets?next=nnnn&previous=ppp&limit=1&siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("GET", response.request().method());

    Presets p = gson.fromJson(response.body(), Presets.class);
    verifyGetPresets(p);
  }

  /**
   * Test GET /presets/{presetId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetPresetsTags01() throws Exception {
    GetPresetTagsRequest req = new GetPresetTagsRequest().presetId(documentId).limit(1).next("nn")
        .previous("pp").siteId(siteId);
    PresetTags tags = this.client.getPresetTags(req);
    verify(tags);
  }

  /**
   * Test GET /presets/{presetId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetPresetsTags02() throws Exception {
    GetPresetTagsRequest req = new GetPresetTagsRequest();
    try {
      this.client.getPresetTags(req);
    } catch (NullPointerException e) {
      assertEquals("PresetId is required.", e.getMessage());
    }
  }

  /**
   * Test GET /presets/{presetId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetPresetsTagsAsHttpResponse() throws Exception {
    GetPresetTagsRequest req = new GetPresetTagsRequest().presetId(documentId).limit(1).next("nn")
        .previous("pp").siteId(siteId);
    HttpResponse<String> response = this.client.getPresetTagsAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(
        URL + "/presets/" + documentId + "/tags?next=nn&previous=pp&limit=1&siteId=" + siteId,
        response.request().uri().toString());

    PresetTags tags = gson.fromJson(response.body(), PresetTags.class);
    verify(tags);
  }

  /**
   * Test GET /sites.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetSites() throws Exception {
    Sites sites = this.client.getSites();
    assertEquals(1, sites.sites().size());
    assertEquals("test@formkiq.com", sites.sites().get(0).uploadEmail());
    assertEquals("adadsad", sites.sites().get(0).siteId());
  }

  /**
   * Test GET /sites.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetSitesAsHttpResponse() throws Exception {
    HttpResponse<String> response = this.client.getSitesAsHttpResponse();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    HttpRequest request = response.request();
    assertEquals(URL + "/sites", request.uri().toString());
    assertEquals("GET", request.method());
    assertEquals("http://localhost", request.headers().firstValue("Origin").get());
    assertTrue(request.headers().firstValue("Authorization").get()
        .startsWith("AWS4-HMAC-SHA256 Credential=123"));
  }

  /**
   * Test GET /version.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetVersion() throws Exception {
    assertEquals("0.0.45", this.client.getVersion().version());
    assertEquals("core", this.client.getVersion().type());
  }

  /**
   * Test GET /version.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetVersionAsHttpResponse() throws Exception {
    HttpResponse<String> response = this.client.getVersionAsHttpResponse();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("0.0.45", gson.fromJson(response.body(), Map.class).get("version").toString());
    assertEquals("core", gson.fromJson(response.body(), Map.class).get("type").toString());
    HttpRequest request = response.request();
    assertEquals(URL + "/version", request.uri().toString());
    assertEquals("GET", request.method());
    assertEquals("http://localhost", request.headers().firstValue("Origin").get());
    assertTrue(request.headers().firstValue("Authorization").get()
        .startsWith("AWS4-HMAC-SHA256 Credential=123"));
  }

  /**
   * Test GET /webhooks.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetWebhooks01() throws Exception {
    GetWebhooksRequest req = new GetWebhooksRequest().siteId(siteId);
    Webhooks doc = this.client.getWebhooks(req);

    assertEquals(1, doc.webhooks().size());
    assertEquals("werwer", "" + doc.webhooks().get(0).id());
    assertEquals("foo", "" + doc.webhooks().get(0).name());
    assertEquals("test", "" + doc.webhooks().get(0).siteId());
    assertEquals("http://localhost:8080/webhooks", "" + doc.webhooks().get(0).url());
    assertEquals("joe", "" + doc.webhooks().get(0).userId());
  }

  /**
   * Test GET /webhooks.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetWebhooksAsHttpResponse() throws Exception {
    GetWebhooksRequest req = new GetWebhooksRequest().siteId(siteId);
    HttpResponse<String> response = this.client.getWebhooksAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/webhooks?siteId=" + siteId, response.request().uri().toString());
    assertEquals("GET", response.request().method());

    Webhooks doc = gson.fromJson(response.body(), Webhooks.class);
    assertEquals(1, doc.webhooks().size());
    assertEquals("werwer", "" + doc.webhooks().get(0).id());
    assertEquals("foo", "" + doc.webhooks().get(0).name());
    assertEquals("test", "" + doc.webhooks().get(0).siteId());
    assertEquals("http://localhost:8080/webhooks", "" + doc.webhooks().get(0).url());
    assertEquals("joe", "" + doc.webhooks().get(0).userId());
  }

  /**
   * Test GET /webhooks/{webhookId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetWebhookTags01() throws Exception {
    GetWebhookTagsRequest req = new GetWebhookTagsRequest().siteId(siteId).webhookId(documentId);
    WebhookTags doc = this.client.getWebhookTags(req);

    assertEquals(1, doc.tags().size());
    assertEquals("category", doc.tags().get(0).key());
    assertEquals("userdefined", doc.tags().get(0).type());
    assertEquals("jsmith", doc.tags().get(0).userId());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", doc.tags().get(0).value());
    assertEquals("999", "" + doc.tags().get(0).webhookId());
    assertNotNull(doc.tags().get(0).insertedDate());
  }

  /**
   * Test GET /webhooks/{webhookId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetWebhookTagsAsHttpResponse() throws Exception {
    GetWebhookTagsRequest req = new GetWebhookTagsRequest().siteId(siteId).webhookId(documentId);
    HttpResponse<String> response = this.client.getWebhookTagsAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/webhooks/" + documentId + "/tags?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("GET", response.request().method());

    WebhookTags doc = gson.fromJson(response.body(), WebhookTags.class);
    assertEquals(1, doc.tags().size());
    assertEquals("category", doc.tags().get(0).key());
    assertEquals("userdefined", doc.tags().get(0).type());
    assertEquals("jsmith", doc.tags().get(0).userId());
    assertEquals("9eb6a07a-08c0-44e0-9d02-a8c6bebb1408", doc.tags().get(0).value());
    assertEquals("999", "" + doc.tags().get(0).webhookId());
    assertNotNull(doc.tags().get(0).insertedDate());
  }

  /**
   * Test OPTIONS /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocument() throws Exception {
    OptionsDocumentRequest req = new OptionsDocumentRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocument(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId, response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /documents/{documentId}/content.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentContent() throws Exception {
    OptionsDocumentContentRequest request =
        new OptionsDocumentContentRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocumentContent(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("OPTIONS", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/content",
        response.request().uri().toString());
  }

  /**
   * Test Options /documents/{documentId}/url.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentContentsUrl() throws Exception {
    OptionsDocumentContentUrlRequest req =
        new OptionsDocumentContentUrlRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocumentContentUrl(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "/url", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /documents/{documentId}/formats.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentFormats() throws Exception {
    OptionsDocumentFormatRequest request =
        new OptionsDocumentFormatRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocumentFormats(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "/formats",
        response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocuments() throws Exception {
    HttpResponse<String> response = this.client.optionsDocuments();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /documents/{documentid}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentsTags() throws Exception {
    OptionsDocumentTagsRequest req = new OptionsDocumentTagsRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocumentTags(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("OPTIONS", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/tags", response.request().uri().toString());
  }

  /**
   * Test OPTIONS /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentTag() throws Exception {
    OptionsDocumentTagsKeyRequest request =
        new OptionsDocumentTagsKeyRequest().documentId(documentId).tagKey("category");
    HttpResponse<String> response = this.client.optionsDocumentTag(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("OPTIONS", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/tags/category",
        response.request().uri().toString());
  }

  /**
   * Test Options /documents/upload.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentUpload01() throws Exception {
    HttpResponse<String> response = this.client.optionsDocumentUpload();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/upload", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test Options /documents/{documentId}/upload.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentUploadWithDocumentId() throws Exception {
    OptionsDocumentUploadRequest req = new OptionsDocumentUploadRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocumentUpload(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("OPTIONS", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/upload", response.request().uri().toString());
  }

  /**
   * Test Options /documents/{documentId}/versions.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumentVersions() throws Exception {
    OptionsDocumentVersionsRequest req =
        new OptionsDocumentVersionsRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocumentVersions(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "/versions",
        response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }


  /**
   * Test OPTIONS /presets/{presetId}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsPreset() throws Exception {
    OptionsPresetRequest req = new OptionsPresetRequest().presetId(documentId);
    HttpResponse<String> response = this.client.optionsPreset(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/presets/" + documentId, response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /presets.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsPresets() throws Exception {
    HttpResponse<String> response = this.client.optionsPresets();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/presets", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /presets/{presetId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsPresetTags() throws Exception {
    OptionsPresetTagsRequest req = new OptionsPresetTagsRequest().presetId(documentId);
    HttpResponse<String> response = this.client.optionsPresetTags(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/presets/" + documentId + "/tags", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /presets/{presetId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsPresetTagsKey() throws Exception {
    OptionsPresetTagsRequest req =
        new OptionsPresetTagsRequest().presetId(documentId).tag("first name");
    HttpResponse<String> response = this.client.optionsPresetTags(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/presets/" + documentId + "/tags/first+name",
        response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /search.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsSearch() throws Exception {
    HttpResponse<String> response = this.client.optionsSearch();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/search", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /sites.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsSites() throws Exception {
    HttpResponse<String> response = this.client.optionsSites();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/sites", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test OPTIONS /version.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsVersion() throws Exception {
    HttpResponse<String> response = this.client.optionsVersion();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/version", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test Options /webhooks.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsWebhooks01() throws Exception {
    HttpResponse<String> response = this.client.optionsWebhooks();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/webhooks", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test Options /webhooks/{webhookId}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsWebhooksId01() throws Exception {
    OptionsWebhookRequest req = new OptionsWebhookRequest().webhookId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.optionsWebhooks(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/webhooks/" + documentId + "?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test Options /webhooks/{webhookId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsWebhookTags01() throws Exception {
    OptionsWebhookTagsRequest req =
        new OptionsWebhookTagsRequest().webhookId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.optionsWebhookTags(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/webhooks/" + documentId + "/tags?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test POST /documents/{documentId}/formats.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testPostDocumentFormats() throws Exception {
    DocumentFormatSearchRequest req = new DocumentFormatSearchRequest().documentId(documentId)
        .mime("bleh").siteId(siteId).siteId("10").versionId("2");
    HttpResponse<String> response = this.client.addDocumentFormatAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/documents/" + documentId + "/formats?siteId=10",
        response.request().uri().toString());
    assertEquals("POST", response.request().method());
  }

  /**
   * Test POST /search.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testSearch01() throws Exception {
    SearchDocumentsRequest req = new SearchDocumentsRequest().tagKey("category").eq("value")
        .limit(1).next("nnn").previous("ppp").siteId(siteId).beginsWith("bbb");
    Documents docs = this.client.search(req);
    assertEquals("123", docs.next());
    assertEquals("345345", docs.previous());
    assertEquals(1, docs.documents().size());
    assertEquals("1000", "" + docs.documents().get(0).contentLength());
    assertEquals("text/plain", docs.documents().get(0).contentType());
    assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", docs.documents().get(0).documentId());
    assertEquals("jkldfkdsjfksdj", docs.documents().get(0).checksum());
    assertEquals("2020/05/05 19:09:09", df.format(docs.documents().get(0).insertedDate()));
    assertEquals("sample/test.txt", docs.documents().get(0).path());
    assertEquals("jtest", docs.documents().get(0).userId());
  }

  /**
   * Test POST /search.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testSearch02() throws Exception {
    SearchDocumentsRequest req = new SearchDocumentsRequest();
    try {
      this.client.search(req);
    } catch (NullPointerException e) {
      assertEquals("TagKey is required.", e.getMessage());
    }
  }

  /**
   * Test POST /search.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testSearchAsHttpResponse() throws Exception {
    SearchDocumentsRequest req = new SearchDocumentsRequest().tagKey("category").eq("value")
        .limit(1).next("nnn").previous("ppp").siteId(siteId).beginsWith("bbb");
    HttpResponse<String> response = this.client.searchAsHttpResponse(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "/search?next=nnn&previous=ppp&limit=1&siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("POST", response.request().method());

    Documents docs = gson.fromJson(response.body(), Documents.class);
    assertEquals("123", docs.next());
    assertEquals("345345", docs.previous());
    assertEquals(1, docs.documents().size());
    assertEquals("1000", "" + docs.documents().get(0).contentLength());
    assertEquals("text/plain", docs.documents().get(0).contentType());
    assertEquals("3fa85f64-5717-4562-b3fc-2c963f66afa6", docs.documents().get(0).documentId());
    assertEquals("jkldfkdsjfksdj", docs.documents().get(0).checksum());
    assertEquals("2020/05/05 19:09:09", df.format(docs.documents().get(0).insertedDate()));
    assertEquals("sample/test.txt", docs.documents().get(0).path());
    assertEquals("jtest", docs.documents().get(0).userId());
  }

  /**
   * Test PATCH /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testUpdateDocuments01() throws Exception {
    UpdateDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), UpdateDocument.class);
    UpdateDocumentRequest request =
        new UpdateDocumentRequest().document(post).documentId(documentId).siteId(siteId);
    UpdateDocumentResponse response = this.client.updateDocument(request);
    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc", response.documentId());
  }

  /**
   * Test PATCH /documents. Missing Data.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testUpdateDocuments02() throws Exception {
    UpdateDocumentRequest request = new UpdateDocumentRequest();
    try {
      this.client.updateDocument(request);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test PATCH /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testUpdateDocumentsAsHttpResponse() throws Exception {
    UpdateDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), UpdateDocument.class);
    UpdateDocumentRequest request =
        new UpdateDocumentRequest().document(post).documentId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.updateDocumentAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("PATCH", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc",
        gson.fromJson(response.body(), Map.class).get("documentId").toString());
  }

  /**
   * Test PUT /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testUpdateDocumentTag01() throws Exception {
    UpdateDocumentTagKeyRequest request = new UpdateDocumentTagKeyRequest().documentId(documentId)
        .tagKey("category").siteId(siteId).tagValue("food");
    assertTrue(this.client.updateDocumentTag(request));
  }

  /**
   * Test PUT /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testUpdateDocumentTag02() throws Exception {
    UpdateDocumentTagKeyRequest request = new UpdateDocumentTagKeyRequest();
    try {
      this.client.updateDocumentTag(request);
    } catch (NullPointerException e) {
      assertEquals("DocumentId is required.", e.getMessage());
    }
  }

  /**
   * Test PUT /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testUpdateDocumentTagAsHttpResponse() throws Exception {
    UpdateDocumentTagKeyRequest request = new UpdateDocumentTagKeyRequest().documentId(documentId)
        .tagKey("category").siteId(siteId).tagValue("food");
    HttpResponse<String> response = this.client.updateDocumentTagAsHttpResponse(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("PUT", response.request().method());
    assertEquals(URL + "/documents/" + documentId + "/tags/category?siteId=" + siteId,
        response.request().uri().toString());
  }

  /**
   * Verify {@link PresetTags}.
   * 
   * @param tags {@link PresetTags}
   */
  private void verify(final PresetTags tags) {
    assertEquals("2131", tags.next());
    assertEquals("asdad", tags.previous());
    assertEquals(1, tags.tags().size());
    assertEquals("2020/08/28 18:39:10", df.format(tags.tags().get(0).insertedDate()));
    assertEquals("dddssd", tags.tags().get(0).key());
    assertEquals("joe", tags.tags().get(0).userId());
  }

  /**
   * Verify {@link Presets}.
   * 
   * @param p {@link Presets}
   */
  private void verifyGetPresets(final Presets p) {
    assertEquals("123", p.next());
    assertEquals("555", p.previous());
    assertEquals(1, p.presets().size());
    assertEquals("foo", "" + p.presets().get(0).name());
    assertEquals("2020/08/28 17:55:20", df.format(p.presets().get(0).insertedDate()));
    assertEquals("werwer", p.presets().get(0).id());
    assertEquals("test", p.presets().get(0).siteId());
    assertEquals("joe", p.presets().get(0).userId());
  }
}
