import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Receiver {
    @GetMapping("/f")
    public Message start(@RequestParam(value = "name", defaultValue = ":)") String name) {
        return new Message("Hello " + name);
    }
}
