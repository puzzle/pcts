# AI Migration & Extraction Service

This service uses AI (**Spring AI**) to analyze `.ods` spreadsheet documents,
extract structured data, and automatically map it to database entities.
The extracted records are then sent directly to the **PCTS API** for creation.

---

## Getting Started

### 1. Start Prerequisites

Ensure the **PCTS application** is running first, as this service depends on its Keycloak container/client for authentication.

### 2. Build & Run

Open your terminal and execute the following commands:

```bash
# Build the project
./mvnw clean compile

# Start the application
./mvnw spring-boot:run
```

## Usage

Upload an .ods spreadsheet via an HTTP POST request to the API endpoint.

>**Important Filename Convention:**
The member's abbreviation must appear at the beginning of the filename.

### Option A: Swagger UI

Once the application is running, open the interactive swagger ui in your browser:

```text
http://localhost:8081/swagger-ui.html
```

### Option B: cURL

```bash
curl -X POST "http://localhost:8080/api/ai/certificates" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@MAXM_certificates.ods"
```
