package existDB.existDB;

import org.xmldb.api.*;
import org.xmldb.api.base.*;
import org.xmldb.api.modules.*;

public class AccesoLibros {
	
	private Collection col = null;
	private XPathQueryService servicio;
	
	//método para conectar con eXistDB
	public void conectar() {
		String driver = "org.exist.xmldb.DatabaseImpl";
		col = null;

		//String uri="xmldb:exist://25.8.244.208:8080/exist/xmlrpc/db";//ip TAMBIÉN FUNCIONA
		// String uri="xmldb:exist://embedded-eXist-server"; //si tenemos la bdd
		// embebida
		String uri = "xmldb:exist://localhost:8080/exist/xmlrpc/db";
		String user = "admin";
		String pass = "1234";
		try {
			//NOS CONECTAMOS
			Class cl = Class.forName(driver);
			System.out.println("Driver encontrado para " + driver);
			Database db = (Database) cl.newInstance();
			System.out.println("Db instancia creada");
			DatabaseManager.registerDatabase(db);
			System.out.println("BDD registrada");
			// nos conectamos con el método getCollection
			// LA VARIABLE COL ESTÁ "ASIGNADA" Y LA PUEDEN USAR EL RESTO DE MÉTODOS
			col = DatabaseManager.getCollection(uri, user, pass);
			System.out.println("Conectados a la base de datos " + uri);
			//COMPROBAMOS SI LA CONEXIÓN TIENE RECURSOS (ARCHIVOS O COLECCIONES)
			if (col == null) {
				System.out.println("---colección no encontrada ---");
			} else {
				System.out.println("Nº de recursos de la colección: "  + col.getResourceCount());
				System.out.println("Primer recurso: "+ col.listResources()[0]); //muestra el primer recurso

				//podríamos hacer un bucle para mostrar todos los recursos
				//desde i=0 hasta col.getResourceCount()
			}
			//OBTENEMOS EL SERVICIO Y LE ASIGNAMOS VALOR
			servicio = (XPathQueryService) col.getService("XPathQueryService", "1.0");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//método para desconectarse
	public void desconectar() {
		try {
			col.close();
			System.out.println("\nDesconexión completada.");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//método que consulta los títulos de books.xml y los muestra por pantalla
	public void consultarTitulos() {
		try {
			ResourceSet result = servicio.query("for $b in /catalog/book/title/text() return $b");
			//VISUALIZAMOS EL RESULTADO DE LA CONSULTA
			System.out.println("\nCONSULTA DE TÍTULOS");
			System.out.println("Numero de resultados: " + result.getSize());
			System.out.println("=================================================");
			ResourceIterator it = result.getIterator();
			while (it.hasMoreResources()) {
				Resource r = (Resource) it.nextResource();
				System.out.println("\t" + (String) r.getContent());
			}
			System.out.println("=================================================");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//método que además muestra sus autores
	public void consultarTitulosYAutor() {
		try {
			//en este caso, mostrará el título y el autor
			ResourceSet result = servicio.query("for $b in /catalog/book return ($b/title/text(),$b/author/text())");
			System.out.println("\nCONSULTA DE TÍTULOS Y AUTORES");
			System.out.println("Numero de resultados: " + result.getSize());
			System.out.println("=================================================");
			ResourceIterator it = result.getIterator();
			//cada loop del while toma dos resources, para mostrar primero el título y después el autor
			while (it.hasMoreResources()) {
				Resource r = (Resource) it.nextResource();
				System.out.print("\tTítulo: " + (String) r.getContent() + "\t---\t"); //sin salto de línea
				r = (Resource) it.nextResource();
				System.out.println("Autor: " + (String) r.getContent());
			}
			System.out.println("=================================================");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//método que recibe el ID y muestra todos los datos del libro
	public void consultarDatosPorId(String id) {
		boolean seHaEncontrado = false; //variable que determinará si se ha encontrado el libro
		try {
			ResourceSet result = servicio.query("for $b in /catalog/book[@id='"+id+"'] return $b");
			System.out.println("\nMUESTRA DE DATOS DEL LIBRO CON ID " + id);
			System.out.println("Numero de resultados: " + result.getSize());
			System.out.println("=================================================");
			ResourceIterator it = result.getIterator();
			while (it.hasMoreResources()) {
				seHaEncontrado = true;
				Resource r = (Resource) it.nextResource();
				System.out.println("\t" + r.getContent());
				//System.out.print("\tTítulo: " + (String) r.getContent() + "\t---\t"); //sin salto de línea
				//r = (Resource) it.nextResource();
				//System.out.println("Autor: " + (String) r.getContent());
			}
			if (!seHaEncontrado) System.out.println("No se ha encontrado el libro");
			System.out.println("=================================================");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//los muestra por ID, pero además sin etiquetas
	public void consultarDatosPorIdSinEtiquetas(String id) {
		boolean seHaEncontrado = false; //variable que determinará si se ha encontrado el libro
		try {
			//en este caso, mostrará el título y el autor
			ResourceSet result = servicio.query("for $b in /catalog/book[@id='"+id+"']"
					+ "return ($b/title/text(),$b/author/text(),$b/genre/text(),$b/price/text(),$b/publish_date/text(),$b/description/text())");
			System.out.println("\nMUESTRA DE DATOS DEL LIBRO CON ID " + id);
			System.out.println("Numero de resultados: " + result.getSize());
			System.out.println("=================================================");
			ResourceIterator it = result.getIterator();
			while (it.hasMoreResources()) {
				seHaEncontrado = true;
				Resource r = (Resource) it.nextResource();
				System.out.print("\tTítulo: " + (String) r.getContent() + "\t---\t"); //sin salto de línea
				r = (Resource) it.nextResource();
				System.out.println("Autor: " + (String) r.getContent());
				r = (Resource) it.nextResource();
				System.out.print("\t\tGénero: " + (String) r.getContent() + "\t---\t");
				r = (Resource) it.nextResource();
				System.out.print("Precio: " + (String) r.getContent() + "\t---\t");
				r = (Resource) it.nextResource();
				System.out.println("Fecha de salida: " + (String) r.getContent());
				r = (Resource) it.nextResource();
				System.out.println("\t\tDescripción: " + (String) r.getContent());
			}
			if (!seHaEncontrado) System.out.println("No se ha encontrado el libro");
			System.out.println("=================================================");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//recibe un título de libro y lo cambia por otro
	public void cambiarTituloLibro(String title, String newTitle) {
		try {
			ResourceSet result = servicio.query("update value /catalog/book[title='" + title + "']/title with '" + newTitle + "'");
			System.out.println("\nCAMBIANDO TÍTULO DEL LIBRO '" + title.toUpperCase() + "' POR '" + newTitle + "'");
			System.out.println("Numero de resultados: " + result.getSize());
			System.out.println("=================================================");
			confirmarTitulo(newTitle); //confirmamos que se ha realizado el cambio
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	//confirma que se ha encontrado un libro o no
	public void confirmarTitulo(String title) {
		boolean seHaEncontrado = false;
		try {
			ResourceSet result = servicio.query("for $b in /catalog/book[title='" + title + "']/title/text() return $b");
			//VISUALIZAMOS EL RESULTADO DE LA CONSULTA
			System.out.println("\nCONFIRMANDO TÍTULO '" + title.toUpperCase() + "'");
			System.out.println("Numero de resultados: " + result.getSize());
			System.out.println("=================================================");
			ResourceIterator it = result.getIterator();
			while (it.hasMoreResources()) {
				seHaEncontrado = true;
				Resource r = (Resource) it.nextResource();
				System.out.println("\t" + (String) r.getContent());
				System.out.println("\tEl libro existe.");
			}
			if (!seHaEncontrado) System.out.println("No existe un libro con ese título");
			System.out.println("=================================================");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}