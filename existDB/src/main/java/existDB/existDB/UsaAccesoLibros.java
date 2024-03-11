package existDB.existDB;

public class UsaAccesoLibros 
{
    public static void main( String[] args )
    {
    	AccesoLibros al = new AccesoLibros();
		al.conectar();
		al.consultarTitulos();
		al.consultarTitulosYAutor();
		al.consultarDatosPorId("bk103");
		al.consultarDatosPorIdSinEtiquetas("bk103");
		al.confirmarTitulo("Midnight Rain");
		//este formato de update value genera excepción por un motivo que desconozco
		al.cambiarTituloLibro("XML Developer's Guide", "Extensive Markup Language Developer's Guide");
		al.desconectar();
    }
}
