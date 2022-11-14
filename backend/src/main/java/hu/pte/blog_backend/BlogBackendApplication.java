package hu.pte.blog_backend;

import hu.pte.blog_backend.models.Category;
import hu.pte.blog_backend.models.Role;
import hu.pte.blog_backend.repository.CategoryRepository;
import hu.pte.blog_backend.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;


@SpringBootApplication
public class BlogBackendApplication implements CommandLineRunner {

    private final RoleRepository roleRepository;

    private final CategoryRepository categoryRepository;

    public BlogBackendApplication(RoleRepository roleRepository, CategoryRepository categoryRepository) {
        this.roleRepository = roleRepository;
        this.categoryRepository = categoryRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(BlogBackendApplication.class, args);
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }

    @Override
    public void run(String... args) {
        if(roleRepository.findByName("ROLE_USER").isEmpty()){
            roleRepository.save(new Role("ROLE_USER"));
        }
        if(roleRepository.findByName("ROLE_ADMIN").isEmpty()){
            roleRepository.save(new Role("ROLE_ADMIN"));
        }
        // XML olvasás
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource("categories.xml").getFile());
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("category");

            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    if(categoryRepository.findByName(eElement.getElementsByTagName("name").item(0).getTextContent()) == null){
                        Category category = new Category();
                        category.setName(eElement.getElementsByTagName("name").item(0).getTextContent());
                        categoryRepository.save(category);
                    }
                }
            }
            }catch (IOException | ParserConfigurationException e){
                e.printStackTrace();
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }

    }
}
