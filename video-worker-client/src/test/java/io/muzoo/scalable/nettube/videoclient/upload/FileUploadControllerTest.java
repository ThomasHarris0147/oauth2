package io.muzoo.scalable.nettube.videoclient.upload;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FileUploadControllerTest {

    @Autowired
    private MockMvc mvc;

    @Order(1)
    @Test
    void uploadFile() throws Exception {
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file",
                "test.mp4",
                "video/mp4",
                "Spring Framework".getBytes());
        this.mvc.perform(multipart("/upload").file(multipartFile))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Uploaded the file successfully: test.mp4\"}"));
        this.mvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{" +
                            "\"name\": \"test.mp4\"," +
                            "\"url\": \"http://localhost/files/test.mp4\"" +
                        "}]"));
    }

    @Order(2)
    @Test
    void getListFiles() throws Exception {
        List<MockMultipartFile> multipartFile = new ArrayList<MockMultipartFile>() {{
            add(new MockMultipartFile("file",
                    "test1.mp4",
                    "video/mp4",
                    "Spring Framework".getBytes()));
            add(new MockMultipartFile("file",
                    "test2.mp4",
                    "video/mp4",
                    "Spring Framework".getBytes()));
            add(new MockMultipartFile("file",
                    "test3.mp4",
                    "video/mp4",
                    "Spring Framework".getBytes()));
        }};
        this.mvc.perform(multipart("/upload").file(multipartFile.get(0)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Uploaded the file successfully: test1.mp4\"}"));
        this.mvc.perform(multipart("/upload").file(multipartFile.get(1)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Uploaded the file successfully: test2.mp4\"}"));
        this.mvc.perform(multipart("/upload").file(multipartFile.get(2)))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"message\": \"Uploaded the file successfully: test3.mp4\"}"));

        this.mvc.perform(get("/files"))
                .andExpect(status().isOk())
                .andExpect(content().json("[" +
                                "{" +
                                    "\"name\": \"test.mp4\"," +
                                    "\"url\": \"http://localhost/files/test.mp4\"" +
                                "}," +
                                "{" +
                                    "\"name\": \"test1.mp4\"," +
                                    "\"url\": \"http://localhost/files/test1.mp4\"" +
                                "}," +
                                "{" +
                                    "\"name\": \"test2.mp4\"," +
                                    "\"url\": \"http://localhost/files/test2.mp4\"" +
                                "}," +
                                "{" +
                                    "\"name\": \"test3.mp4\"," +
                                    "\"url\": \"http://localhost/files/test3.mp4\"" +
                                "}" +
                        "]"));
    }

    @Order(3)
    @Test
    void getFile() {
        // does nothing atm
    }
}