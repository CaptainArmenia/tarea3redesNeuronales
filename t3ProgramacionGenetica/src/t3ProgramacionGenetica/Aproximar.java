package t3ProgramacionGenetica;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class Aproximar {
	int numero;
	int[] numeros;
	String[] operaciones;
	int sizeGeneracion;
	double tasaMutacion;
	AST[] generacion;
	int nSobrevivientes;
	
	public Aproximar(int x, int[] numeros, String[] operaciones, int n, double mr) {
		this.numero = x;
		this.numeros = numeros;
		this.operaciones = operaciones;
		this.sizeGeneracion = n;
		this.tasaMutacion = mr;
		this.nSobrevivientes = n / 4 + 1;
		generacion = new AST[n];
		for(int k = 0; k < n; k++) {
			generacion[k] = new AST(5, numeros, operaciones, true);
		}
	}
	
	public void ejecutar() {
		System.out.println("numero objetivo: " + this.numero);
		
		//identificamos palabra mas cercana
		int distancia = Math.abs(this.numero - this.getMasCercana().evaluar());
		
		int generaciones = 1;
		
		while(distancia > 0) {
			//seleccionamos a los sobrevivientes matando a los mas debiles
			ArrayList<AST> sobrevivientes = new ArrayList<AST>(Arrays.asList(this.generacion));
			int nMuertes = this.generacion.length - this.nSobrevivientes;
			for(int i = 0; i < nMuertes; i++) {
				int indiceMax = 0;
				int distanciaMax = 0;
				for(int k = 0; k < sobrevivientes.size(); k++) {
					int distanciaCandidata = Math.abs(this.numero - generacion[i].evaluar());
					if(distanciaCandidata > distanciaMax) {
						indiceMax = k;
						distanciaMax = distanciaCandidata;
					}
				}
				sobrevivientes.remove(indiceMax);
			}

			//reproducimos a los sobrevivientes
			AST[] nuevaGeneracion = new AST[this.sizeGeneracion];
			for(int k = 0; k < this.generacion.length; k++) {
				int i = ThreadLocalRandom.current().nextInt(0, sobrevivientes.size());
				int j = ThreadLocalRandom.current().nextInt(0, sobrevivientes.size());
				AST hijo = sobrevivientes.get(i).cruzar(sobrevivientes.get(j)).mutar(this.tasaMutacion);
				nuevaGeneracion[k] = hijo;
			}
			
			//actualizamos poblacion
			AST[] generacionAntigua = new AST[this.sizeGeneracion];
			this.generacion = nuevaGeneracion;
			generaciones++;
			distancia = Math.abs(this.numero - this.getMasCercana().evaluar());
			
		}
		System.out.println("generaciones producidas: " + generaciones);
		System.out.println("tamanio de cada generacion: " + this.generacion.length);
		System.out.println("tasa de mutacion: " + this.tasaMutacion);
		System.out.println("distancia: " + distancia);
		BTreePrinter.printNode(this.getMasCercana());
	}
	
	public AST getMasCercana() {
		//identificamos palabra mas cercana
		int distancia = 0;
		int indice = 0;
		for(int i = 0; i < this.generacion.length; i++) {
			int distanciaCandidata = Math.abs(this.numero - this.generacion[i].evaluar());
			if(i == 0 || distanciaCandidata < distancia) {
				distancia = distanciaCandidata;
				indice = i;
			}
		}
		return this.generacion[indice];
	}
	
	public AST getMasCercana(AST[] generacion) {
		//identificamos palabra mas cercana
		int distancia = 0;
		int indice = 0;
		for(int i = 0; i < generacion.length; i++) {
			int distanciaCandidata = Math.abs(this.numero - generacion[i].evaluar());
			if(i == 0 || distanciaCandidata < distancia) {
				distancia = distanciaCandidata;
				indice = i;
			}
		}
		return generacion[indice];
	}
	
}
