import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SensacionTermica {

    public static void main(String[] args) {
    	   // Iniciar los procesos TemperatureSensor y WindSpeedSensor como comandos separados
        try {
            // Iniciar TemperatureSensor
            Runtime.getRuntime().exec( "java -jar C:\\Users\\Quebim\\Documents\\workspace-spring-tool-suite-4-4.19.1.RELEASE\\bin\\TemperatureSensor.jar");
            // Iniciar WindSpeedSensor
            Runtime.getRuntime().exec("java -jar C:\\\\Users\\\\Quebim\\\\Documents\\\\workspace-spring-tool-suite-4-4.19.1.RELEASE\\\\bin\\WindSpeedSensor.jar");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Esperar un tiempo para asegurarse de que los procesos han generado los primeros archivos
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int i = 1;

        while (true) {
            String archivoTemperatura = "TemperatureData_" + i + ".dat";
            String archivoViento = "WindSpeedData_" + i + ".dat";

            // Leer archivos de temperatura y viento
            double temperatura = leerArchivoTemperatura(archivoTemperatura);
            double viento = leerArchivoViento(archivoViento);

            // Calcular sensación térmica
            double sensacionTermica = calcularSensacionTermica(temperatura, viento);

            // Mostrar datos por consola
            System.out.println("----------------------------------------------------------");
            System.out.println("Temperatura: " + temperatura + " C");
            System.out.println("Velocidad del viento: " + viento + " Kh");
            System.out.printf("Sensacion termica: %.2f C%n", sensacionTermica);
            System.out.println("----------------------------------------------------------");
            System.out.println();

            // Eliminar archivos después de la lectura
            borrarArchivo(archivoTemperatura);
            borrarArchivo(archivoViento);

            // Esperar 5 segundos antes de la siguiente lectura
            try {
            	  Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            i++;
        }
    
    }
    
    

    private static double leerArchivoTemperatura(String nombreArchivo) {
        double temperatura = 0.0; // Valor por defecto en caso de error o archivo no encontrado

        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea = lector.readLine();
            if (linea != null) {
                temperatura = Double.parseDouble(linea); // Convertir la línea a un valor numérico
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            // Manejo de excepciones: En caso de error, puedes registrar la excepción o tomar otra acción adecuada.
        }

        return temperatura;
    }

    private static double leerArchivoViento(String nombreArchivo) {
        double velocidadViento = 0.0; // Valor por defecto en caso de error o archivo no encontrado

        try (BufferedReader lector = new BufferedReader(new FileReader(nombreArchivo))) {
            String linea = lector.readLine();
            if (linea != null) {
                velocidadViento = Double.parseDouble(linea); // Convertir la línea a un valor numérico
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            // Manejo de excepciones: En caso de error, puedes registrar la excepción o tomar otra acción adecuada.
        }

        return velocidadViento;
    }
    private static double calcularSensacionTermica(double temperatura, double velocidadViento) {
        return (0.045 * (5.27 * Math.sqrt(velocidadViento) + 10.45 - 0.28 * velocidadViento) * (temperatura - 33) + 33);
       
    }

    private static void borrarArchivo(String nombreArchivo) {
        File archivo = new File(nombreArchivo);
        if (archivo.exists()) {
            archivo.delete();
        }
    }
}
