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
import com.formkiq.stacks.client.models.Document;
import com.formkiq.stacks.client.models.DocumentTag;
import com.formkiq.stacks.client.models.DocumentTags;
import com.formkiq.stacks.client.models.DocumentUrl;
import com.formkiq.stacks.client.models.Documents;
import com.formkiq.stacks.client.models.NewDocument;
import com.formkiq.stacks.client.models.UpdateDocument;
import com.formkiq.stacks.client.requests.AddDocumentRequest;
import com.formkiq.stacks.client.requests.AddDocumentTagRequest;
import com.formkiq.stacks.client.requests.DeleteDocumentRequest;
import com.formkiq.stacks.client.requests.DeleteDocumentTagRequest;
import com.formkiq.stacks.client.requests.GetDocumentContentUrlRequest;
import com.formkiq.stacks.client.requests.GetDocumentRequest;
import com.formkiq.stacks.client.requests.GetDocumentTagsKeyRequest;
import com.formkiq.stacks.client.requests.GetDocumentTagsRequest;
import com.formkiq.stacks.client.requests.GetDocumentUploadRequest;
import com.formkiq.stacks.client.requests.GetDocumentsRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentContentUrlRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentTagsKeyRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentTagsRequest;
import com.formkiq.stacks.client.requests.OptionsDocumentUploadRequest;
import com.formkiq.stacks.client.requests.SearchDocumentsRequest;
import com.formkiq.stacks.client.requests.UpdateDocumentRequest;
import com.formkiq.stacks.client.requests.UpdateDocumentTagKeyRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;

/**
 * 
 * Unit Test for {@link FormKiqHttpClient}.
 *
 */
public class FormKiqHttpClientTest {

  /** Date Format. */
  private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
  /** Http Status OK. */
  private static final int HTTP_STATUS_OK = 200;
  /** Http Status Created. */
  private static final int HTTP_STATUS_CREATED = 201;
  /** Random Site Identifier. */
  private static String siteId = UUID.randomUUID().toString();
  /** Random Document Identifier. */
  private static String documentId = UUID.randomUUID().toString();
  /** Port to run Test server. */
  private static final int PORT = 8080;
  /** Test server URL. */
  private static final String URL = "http://localhost:" + PORT + "/";
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
    add("get", "/documents", "/get_documents.json");
    add("post", "/documents", "/documentsId.json");
    add("options", "/documents", "/documentsId.json");
    add("patch", "/documents/" + documentId, "/documentsId.json");
    add("get", "/documents/" + documentId, "/get_document.json");
    add("delete", "/documents/" + documentId, "/documentsId.json");
    add("get", "/documents/" + documentId + "/tags", "/get_documents_tags.json");
    mockServer.when(request().withMethod("post").withPath("/documents/" + documentId + "/tags"))
        .respond(
            org.mockserver.model.HttpResponse.response(resourceToString("/documentsId.json", UTF_8))
                .withStatusCode(Integer.valueOf(HTTP_STATUS_CREATED)));
    add("options", "/documents/" + documentId + "/tags", "/documentsId.json");
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
    add("post", "/search", "/search.json");
    add("options", "/search", "/search.json");
  }

  /**
   * After Class.
   */
  @AfterClass
  public static void stopServer() {
    mockServer.stop();
  }

  /** {@link FormKiqHttpClientConnection}. */
  private FormKiqHttpClientConnection connection = new FormKiqHttpClientConnection(URL)
      .region(Region.US_EAST_1).credentials(AwsBasicCredentials.create("123", "444"))
      .header("origin", Arrays.asList("http://localhost")).cognitoIdToken("AAAA");

  /** {@link FormKiqHttpClient}. */
  private FormKiqHttpClient client = new FormKiqHttpClient(this.connection);

  /**
   * Test POST /documents.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocuments01() throws Exception {
    NewDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), NewDocument.class);
    AddDocumentRequest req = new AddDocumentRequest().document(post).siteId(siteId);
    HttpResponse<String> response = this.client.addDocument(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents?siteId=" + siteId, response.request().uri().toString());
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
  public void testAddDocuments02() throws Exception {
    NewDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), NewDocument.class);
    post.contentType(null);
    AddDocumentRequest req = new AddDocumentRequest().document(post);
    HttpResponse<String> response = this.client.addDocument(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents", response.request().uri().toString());
    assertEquals("POST", response.request().method());

    assertEquals("3de5c199-0537-4bb3-a035-aa2367a8bddc",
        gson.fromJson(response.body(), Map.class).get("documentId").toString());
    HttpRequest request = response.request();
    assertNull(request.headers().map().get("Content-Type"));
  }

  /**
   * Test POST /documents/{documentId}/tags.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testAddDocumentTag() throws Exception {
    AddDocumentTagRequest req = new AddDocumentTagRequest().siteId(siteId).documentId(documentId);
    HttpResponse<String> response = this.client.addDocumentTag(req, "category", "person");
    assertEquals(HTTP_STATUS_CREATED, response.statusCode());
    assertEquals("POST", response.request().method());
    assertEquals(URL + "documents/" + documentId + "/tags", response.request().uri().toString());
  }

  /**
   * Test DELETE /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocument() throws Exception {
    DeleteDocumentRequest request =
        new DeleteDocumentRequest().documentId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.deleteDocument(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents/" + documentId + "?siteId=" + siteId,
        response.request().uri().toString());
    assertEquals("DELETE", response.request().method());
  }

  /**
   * Test DELETE /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testDeleteDocumentTag() throws Exception {
    DeleteDocumentTagRequest request =
        new DeleteDocumentTagRequest().documentId(documentId).tagKey("category").siteId(siteId);
    HttpResponse<String> response = this.client.deleteDocumentTag(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("DELETE", response.request().method());
    assertEquals(URL + "documents/" + documentId + "/tags/category",
        response.request().uri().toString());
  }

  /**
   * Test GET /documents/{documentid}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocument() throws Exception {
    GetDocumentRequest req = new GetDocumentRequest().documentId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.getDocument(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents/" + documentId + "?siteId=" + siteId,
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
   * Test GET /documents/{documentId}/url.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentContentUrl() throws Exception {
    GetDocumentContentUrlRequest req =
        new GetDocumentContentUrlRequest().documentId(documentId).duration(1).siteId(siteId);
    HttpResponse<String> response = this.client.getDocumentContentUrl(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents/" + documentId + "/url?duration=1&siteId=" + siteId,
        response.request().uri().toString());
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
    HttpResponse<String> response = this.client.getDocuments(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(
        URL + "documents?next=nnnn&date=2020-02-01&previous=ppp&tz=0500&limit=1&siteId=" + siteId,
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
  public void testGetDocumentsTags() throws Exception {
    GetDocumentTagsRequest req = new GetDocumentTagsRequest().documentId(documentId).limit(1)
        .next("nn").previous("pp").siteId(siteId);
    HttpResponse<String> response = this.client.getDocumentTags(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents/" + documentId + "/tags", response.request().uri().toString());

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
   * Test GET /documents/{documentId}/tags/{tagKey}.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetDocumentTag() throws Exception {
    GetDocumentTagsKeyRequest request =
        new GetDocumentTagsKeyRequest().documentId(documentId).tagKey("category").siteId(siteId);
    HttpResponse<String> response = this.client.getDocumentTag(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("GET", response.request().method());
    assertEquals(URL + "documents/" + documentId + "/tags/category",
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
    HttpResponse<String> response = this.client.getDocumentUpload(new GetDocumentUploadRequest()
        .contentLength(1).documentId(null).duration(2).path("test.txt"));
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents/upload?duration=2&path=test.txt&contentLength=1",
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
  public void testGetDocumentUpload02() throws Exception {
    GetDocumentUploadRequest req = new GetDocumentUploadRequest().documentId(documentId)
        .siteId(siteId).contentLength(1).duration(2).path("test.txt");
    HttpResponse<String> response = this.client.getDocumentUpload(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("GET", response.request().method());
    assertEquals(URL + "documents/" + documentId + "/upload?duration=2&path=test.txt&siteId="
        + siteId + "&contentLength=1", response.request().uri().toString());

    DocumentUrl url = gson.fromJson(response.body(), DocumentUrl.class);
    assertEquals("3c39bb05-9c7a-4afa-8497-6935a1e8dbae", url.documentId());
    assertEquals("https://www.google.com", url.url());
  }

  /**
   * Test GET /version.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testGetVersion() throws Exception {
    HttpResponse<String> response = this.client.getVersion();
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("0.0.45", gson.fromJson(response.body(), Map.class).get("message").toString());
    HttpRequest request = response.request();
    assertEquals(URL + "version", request.uri().toString());
    assertEquals("GET", request.method());
    assertEquals("http://localhost", request.headers().firstValue("Origin").get());
    assertTrue(request.headers().firstValue("Authorization").get()
        .startsWith("AWS4-HMAC-SHA256 Credential=123"));
  }

  /**
   * Test Options /documents/{documentId}/url.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testOptionsDocumenContentsUrl() throws Exception {
    OptionsDocumentContentUrlRequest req =
        new OptionsDocumentContentUrlRequest().documentId(documentId);
    HttpResponse<String> response = this.client.optionsDocumentContentUrl(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "documents/" + documentId + "/url", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
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
    assertEquals(URL + "documents/" + documentId, response.request().uri().toString());
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
    assertEquals(URL + "documents", response.request().uri().toString());
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
    assertEquals(URL + "documents/" + documentId + "/tags", response.request().uri().toString());
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
    assertEquals(URL + "documents/" + documentId + "/tags/category",
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
    assertEquals(URL + "documents/upload", response.request().uri().toString());
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
    assertEquals(URL + "documents/" + documentId + "/upload", response.request().uri().toString());
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
    assertEquals(URL + "search", response.request().uri().toString());
    assertEquals("OPTIONS", response.request().method());
  }

  /**
   * Test POST /search.
   * 
   * @throws Exception Exception
   */
  @Test
  public void testSearch() throws Exception {
    SearchDocumentsRequest req = new SearchDocumentsRequest().tagKey("category").eq("value")
        .limit(1).next("nnn").previous("ppp").siteId(siteId).beginsWith("bbb");
    HttpResponse<String> response = this.client.search(req);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals(URL + "search?next=nnn&previous=ppp&limit=1&siteId=" + siteId,
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
  public void testUpdateDocuments() throws Exception {
    UpdateDocument post =
        gson.fromJson(resourceToString("/post_documents.json", UTF_8), UpdateDocument.class);
    UpdateDocumentRequest request =
        new UpdateDocumentRequest().document(post).documentId(documentId).siteId(siteId);
    HttpResponse<String> response = this.client.updateDocument(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("PATCH", response.request().method());
    assertEquals(URL + "documents/" + documentId + "?siteId=" + siteId,
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
  public void testUpdateDocumentTag() throws Exception {
    UpdateDocumentTagKeyRequest request = new UpdateDocumentTagKeyRequest().documentId(documentId)
        .tagKey("category").siteId(siteId).tagValue("food");
    HttpResponse<String> response = this.client.updateDocumentTag(request);
    assertEquals(HTTP_STATUS_OK, response.statusCode());
    assertEquals("PUT", response.request().method());

    assertEquals(URL + "documents/" + documentId + "/tags/category",
        response.request().uri().toString());
  }
}