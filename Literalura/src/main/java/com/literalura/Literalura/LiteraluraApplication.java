package com.literalura.Literalura;

import com.literalura.Literalura.model.AuthorEntity;
import com.literalura.Literalura.model.BookEntity;
import com.literalura.Literalura.repository.BookRepository;
import com.literalura.Literalura.repository.AuthorRepository;
import com.literalura.Literalura.service.BookService;
import com.literalura.Literalura.service.AuthorService;
import com.literalura.Literalura.service.ApiService;
import com.literalura.Literalura.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

	@Autowired
	private BookRepository bookRepository;
	@Autowired
	private AuthorRepository authorRepository;
	@Autowired
	private BookService bookService;
	@Autowired
	private AuthorService authorService;
	@Autowired
	private ApiService apiService;

	public static void main(String[] args) {
		SpringApplication.run(LiteraluraApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		while (true) {
			displayMenu();
			int choice = scanner.nextInt();
			scanner.nextLine(); // Consume newline
			switch (choice) {
				case 1:
					System.out.print("Introduce el título del libro: ");
					String title = scanner.nextLine();
					searchAndSaveBooksByTitle(title);
					break;
				case 2:
					listAllBooks();
					break;
				case 3:
					listAuthors();
					break;
				case 4:
					System.out.print("Introduce el año para listar autores vivos: ");
					int year = scanner.nextInt();
					listAuthorsAliveInYear(year);
					break;
				case 5:
					bookService.showStatistics();
					break;
				case 6:
					List<BookEntity> topBooks = bookService.getTop10DownloadedBooks();
					System.out.println("Top 10 libros más descargados:");
					for (int i = 0; i < topBooks.size(); i++) {
						System.out.println((i + 1) + ". " + topBooks.get(i).getTitle() + " - Descargas: " + topBooks.get(i).getDownloads());
					}
					break;
				case 7:
					System.out.print("Introduce el nombre del autor: ");
					String authorName = scanner.nextLine();
					List<AuthorEntity> authors = authorService.findAuthorsByName(authorName);
					if (authors.isEmpty()) {
						System.out.println("No se encontraron autores con ese nombre.");
					} else {
						authors.forEach(author -> System.out.println(author.getName()));
					}
					break;
				case 8:
					System.out.println("Saliendo del programa...");
					System.exit(0);
				default:
					System.out.println("Opción no válida.");
			}
		}
	}

	private void displayMenu() {
		System.out.println("\n--- Menú Literalura ---");
		System.out.println("1. Buscar y guardar libro por título");
		System.out.println("2. Listar todos los libros");
		System.out.println("3. Listar autores");
		System.out.println("4. Listar autores vivos en un año específico");
		System.out.println("5. Mostrar estadísticas por idioma");
		System.out.println("6. Mostrar Top 10 libros más descargados");
		System.out.println("7. Buscar autor por nombre");
		System.out.println("8. Salir");
		System.out.print("Selecciona una opción: ");
	}

	private void searchAndSaveBooksByTitle(String title) throws IOException, InterruptedException {
		List<Book> books = apiService.fetchBooks(title);
		if (books.isEmpty()) {
			System.out.println("No se encontraron libros con ese título.");
		} else {
			books.forEach(book -> {
				bookService.saveBook(book);
				System.out.println("Guardado: " + book.getTitle());
			});
			System.out.println("Libros guardados en la base de datos.");
		}
	}

	private void listAllBooks() {
		List<BookEntity> books = bookService.findAllBooks();
		if (books.isEmpty()) {
			System.out.println("No hay libros en la base de datos.");
		} else {
			books.forEach(book -> System.out.println(book.getTitle()));
		}
	}

	private void listAuthors() {
		authorRepository.findAll().forEach(author -> System.out.println(author.getName()));
	}

	private void listAuthorsAliveInYear(int year) {
		List<AuthorEntity> authors = authorService.findAuthorsAliveInYear(year);
		if (authors.isEmpty()) {
			System.out.println("No se encontraron autores vivos en ese año.");
		} else {
			authors.forEach(author -> System.out.println(author.getName()));
		}
	}
}