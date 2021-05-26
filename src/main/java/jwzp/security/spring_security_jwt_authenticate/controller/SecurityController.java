package jwzp.security.spring_security_jwt_authenticate.controller;

import jwzp.security.spring_security_jwt_authenticate.models.Book;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class SecurityController {

    private final List<Book> booksList;

    public SecurityController() {
        booksList = new ArrayList<>();
        booksList.add(new Book("Krzyżacy", "Henryk Sienkiewicz", "GREG"));
        booksList.add(new Book("Hobbit", "John Ronald Reuel Tolkien", "Iskra"));
        booksList.add(new Book("Czterej pancerni i pies", "Janusz Przymanowski", "Vesper"));
    }

    //Dostępne dla każdego
    @GetMapping("/books")
    public List<Book> getBooks() {
        return booksList;
    }

    //Dostępne dla uwierzytelnionego użytkownika
    @PostMapping("/books")
    public boolean addBook(@RequestBody Book book) {
        return booksList.add(book);
    }

    //Dostępne dla uwierzytelnionego użytkownika i zautoryzowanego jako admin
    @DeleteMapping("/books/{id}")
    public void deleteBook(@PathVariable int id) {
        booksList.remove(id);
    }
}
