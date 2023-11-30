import java.util.ArrayList;
import java.util.List;
import java.util.Random;



public class AlgoritmoGeneticoDemo {

    public static void main(String[] args) {
        int numeroPoblaciones = 6;
        int tamanoPoblacion = 9;
        boolean BUscarFitness = false;
        int generacion = 0;
        int maxGeneracion = 10;

        while (!BUscarFitness) {
            // Lista de todas las poblaciones
            ArrayList<ArrayList<Integer>> LasPoblaciones = new ArrayList<>();

            for (int i = 0; i < numeroPoblaciones; i++) {
                ArrayList<Integer> poblacion = crearPoblacionAleatoria(tamanoPoblacion);
                LasPoblaciones.add(poblacion);

                int fitness = CalFitness(LasPoblaciones.get(i));
                System.out.println("Población " + (i + 1) + ": |  " + LasPoblaciones.get(i) + " | Fitness: " + fitness);

                if (fitness == 9) {
                    System.out.println("¡Fitness deseado encontrado en la generación " + generacion + "!");
                    BUscarFitness = true;
                    break;
                }
            }

            if (!BUscarFitness) {
                ArrayList<Integer> mejorPoblacionFit = FitnessDeseado(LasPoblaciones);
                System.out.println("\nPadre: " + mejorPoblacionFit);

                int totalFitness = TFitness(LasPoblaciones);
                ArrayList<Double> porcentajes = Porcentajee(LasPoblaciones);
                ArrayList<Integer> seleccionRuleta = Ruleta.IndividuoR(LasPoblaciones, porcentajes, mejorPoblacionFit);
                System.out.println("Madre: " + seleccionRuleta);

                ArrayList<ArrayList<Integer>> hijos = Crossover(mejorPoblacionFit, seleccionRuleta, LasPoblaciones);
                LasPoblaciones.clear();
                LasPoblaciones.addAll(hijos);

                for (ArrayList<Integer> hijo : LasPoblaciones) {
                    //System.out.println("Hijo resultante: " + hijo);
                }

                Mutacion(LasPoblaciones.get(0), LasPoblaciones.get(1), LasPoblaciones);

                for (ArrayList<Integer> hijo : LasPoblaciones) {
                    //System.out.println("Hijo después de la mutación: " + hijo);
                }
            }

            generacion++;
        }
    }

    public static void Mutacion(ArrayList<Integer> hijo, ArrayList<Integer> hijo2, ArrayList<ArrayList<Integer>> LasPoblaciones) {
        Random random = new Random();

        int CromosomaHijo1 = random.nextInt(hijo.size());

        int valorCromosoma1 = hijo.get(CromosomaHijo1);
        if (valorCromosoma1 == 0) {
            hijo.set(CromosomaHijo1, 1);
        } else {
            hijo.set(CromosomaHijo1, 0);
        }

        int CromosomaHijo2 = random.nextInt(hijo2.size());

        int valorCromosoma2 = hijo2.get(CromosomaHijo2);
        if (valorCromosoma2 == 0) {
            hijo2.set(CromosomaHijo2, 1);
        } else {
            hijo2.set(CromosomaHijo2, 0);
        }

    }

class Individuo {
    int[] genes;
    int fitness;

    public Individuo(int[] genes, int fitness) { // Clase que representa un individuo con genes y su aptitud (fitness)
        this.genes = genes;
        this.fitness = fitness;
    }
}

 class Ruleta { // Clase que implementa la lógica de selección de individuos mediante ruleta

    public static ArrayList<Integer> IndividuoR(
            ArrayList<ArrayList<Integer>> poblaciones,
            ArrayList<Double> porcentajes,
            ArrayList<Integer> mejorPoblacionfit) {

        double valorRuleta = Math.random() * 100; //Genera un valor
        double sumaPorcentajes = 0;

        for (int i = 0; i < porcentajes.size(); i++) {
            sumaPorcentajes += porcentajes.get(i);
            if (valorRuleta <= sumaPorcentajes) {
                // Seleccionar el individuo correspondiente a este rango de porcentaje
                return poblaciones.get(i);
            }
        }

        // Si no se seleccionó ningún individuo (esto debería ser poco probable)
        // Se devuelve la mejor población si existe
        return (mejorPoblacionfit != null) ? mejorPoblacionfit : poblaciones.get(0);
    }
}

/*
class Mutacion {

    public static void mutar(Individuo individuo, double tasaMutacion) {
        for (int i = 0; i < individuo.genes.length; i++) {
            if (Math.random() < tasaMutacion) {
                individuo.genes[i] = 1 - individuo.genes[i]; // Cambiar 0 a 1 o 1 a 0
            }
        }
    }
}*/
/*
class Crossover {

    public static Individuo cruzar(Individuo padre, Individuo madre) {
        int puntoCorte = new Random().nextInt(padre.genes.length);

        int[] genesHijo = new int[padre.genes.length];

        for (int i = 0; i < puntoCorte; i++) {
            genesHijo[i] = padre.genes[i];
        }

        for (int i = puntoCorte; i < madre.genes.length; i++) {
            genesHijo[i] = madre.genes[i];
        }

        return new Individuo(genesHijo, 0); // La aptitud  o fitness se calculará más adelante en el algoritmo genético
    }
}*/







    // Método de ejemplo para calcular el fitness
    public static int CalFitness(ArrayList<Integer> poblacion) {
        int contadorUnos = 0;

        for (int numero : poblacion) {
            if (numero == 1) {
                contadorUnos++;
            }
        }
        return contadorUnos;
    }

    public static int TFitness(ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        int totalFitness = 0;

        for (ArrayList<Integer> poblacion : todasLasPoblaciones) {
            int fitness = CalFitness(poblacion);
            totalFitness += fitness;
        }
        return totalFitness;
    }


    public static ArrayList<Integer> FitnessDeseado(ArrayList<ArrayList<Integer>> todasLasPoblaciones) {
        int mejorFitness = -1;
        ArrayList<Integer> mejorPoblacionfit = null;

        for (ArrayList<Integer> poblacion : todasLasPoblaciones) {
            int fitness = CalFitness(poblacion);
            // encontrar una población con un fitness mejor
            if (fitness > mejorFitness) {
                mejorFitness = fitness;
                mejorPoblacionfit = poblacion;
            }
        }

        return mejorPoblacionfit;
    }
    public static ArrayList<Double> Porcentajee(ArrayList<ArrayList<Integer>> todasLasPoblaciones) { // todo el porcentaje
        int totalFitness = TFitness(todasLasPoblaciones);
        ArrayList<Double> porcentajes = new ArrayList<>();

        for (ArrayList<Integer> poblacion : todasLasPoblaciones) {
            int fitness = CalFitness(poblacion);
            double porcentaje = (double) fitness / totalFitness * 100;
            porcentajes.add(porcentaje);
        }

        return porcentajes;
    }

    // Método de utilidad para imprimir un array de genes
    private static String arrayToString(int[] array) {
        StringBuilder result = new StringBuilder();
        for (int gen : array) {
            result.append(gen).append(" ");
        }
        return result.toString();
    }
    public static ArrayList<Integer> crearPoblacionAleatoria(int tamanioPoblacion) {
        ArrayList<Integer> poblacion = new ArrayList<>();
        Random random = new Random();

        for (int i = 0; i < tamanioPoblacion; i++) {
            int valorAleatorioPoblacion = random.nextInt(2);
            poblacion.add(valorAleatorioPoblacion);
        }

        return poblacion;
    }




    public static ArrayList<ArrayList<Integer>> Crossover(ArrayList<Integer> mejorPoblacion, ArrayList<Integer> seleccionRuleta, ArrayList<ArrayList<Integer>> LasPoblaciones) {
        int realizar = 50, Mutar = 50;
        Random random = new Random();
        int Valor = random.nextInt(101);

        if (Valor > realizar) {
            // Si se hace el crossover
            System.out.println("------CrossOver Hecho-------");
            ArrayList<ArrayList<Integer>> hijos = Crossoverr(mejorPoblacion, seleccionRuleta, LasPoblaciones);

            int Valor2 = random.nextInt(101);
            if (Valor2 > Mutar) {
                System.out.println("-------Mutacion Hecha------");
               Mutacion(hijos.get(0), hijos.get(1), LasPoblaciones);
            } else {
                System.out.println("-----MUTACION NO HECHA-------");
            }

            return hijos;
        } else {
            // No se Hace el CrossOver
            System.out.println("--------CROSSOVER NO HECHO-------");
            ArrayList<Double> porcentajes = Porcentajee(LasPoblaciones);
            seleccionRuleta = Ruleta.IndividuoR(LasPoblaciones, porcentajes, mejorPoblacion);
            System.out.println("Nueva madre: \n" + seleccionRuleta);

            ArrayList<ArrayList<Integer>> hijos = Crossoverr(mejorPoblacion, seleccionRuleta, LasPoblaciones);

            Mutacion(hijos.get(0), hijos.get(1), LasPoblaciones);
            return hijos;
        }
    }


    private static ArrayList<ArrayList<Integer>> Crossoverr(ArrayList<Integer> Poblacionfit, ArrayList<Integer> seleccionRuleta, ArrayList<ArrayList<Integer>> LasPoblaciones) {
        int cuantosValores = new Random().nextInt(Poblacionfit.size());

        ArrayList<Integer> hijo = new ArrayList<>();
        ArrayList<Integer> hijoo = new ArrayList<>();


        for (int i = 0; i < cuantosValores; i++) {
            int temp = Poblacionfit.get(i);
            Poblacionfit.set(i, seleccionRuleta.get(i));
            seleccionRuleta.set(i, temp);
        }


        hijo.addAll(Poblacionfit.subList(0, cuantosValores));
        hijo.addAll(seleccionRuleta.subList(cuantosValores, seleccionRuleta.size()));


        for (int i = 0; i < cuantosValores; i++) {
            int temp = Poblacionfit.get(i);
            Poblacionfit.set(i, seleccionRuleta.get(i));
            seleccionRuleta.set(i, temp);
        }

        for (int i = cuantosValores; i < seleccionRuleta.size(); i++) {
            int temp = Poblacionfit.get(i);
            Poblacionfit.set(i, seleccionRuleta.get(i));
            seleccionRuleta.set(i, temp);
        }


        hijoo.addAll(Poblacionfit.subList(0, cuantosValores));
        hijoo.addAll(seleccionRuleta.subList(cuantosValores, seleccionRuleta.size()));

        ArrayList<ArrayList<Integer>> hijos = new ArrayList<>();
        hijos.add(hijo);
        hijos.add(hijoo);



        return hijos;
    }
    // Clase que implementa la lógica de mutación de un individuo

}
