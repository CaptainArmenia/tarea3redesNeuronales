package t3ProgramacionGenetica;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AST {
	public String val;
	public AST izq;
	public AST der;
	public int profundidad;
	public String[] ops;
	public int[] vals;
	
	//constructor estandar
	public AST(String operacion, AST hijo1, AST hijo2, String[] operaciones, int[] valores) {
		this.val = operacion;
		this.izq = hijo1;
		this.der = hijo2;
		this.ops = operaciones;
		this.vals = valores;
		if(hijo1 ==  null && hijo2 == null) {
			this.profundidad = 1;
		}
		else if(hijo1 == null) {
			this.profundidad = 1 + hijo2.profundidad;
		}
		else if(hijo2 == null) {
			this.profundidad = 1 + hijo1.profundidad;
		}
		else {
			int prof1 = hijo1.profundidad;
			int prof2 = hijo2.profundidad;
			
			if(prof1 > prof2) {
				this.profundidad = 1 + prof1;
			}
			else {
				this.profundidad = 1 + prof2;
			}
		}
	}
	
	//constructor para hoja
	public AST(int valor, String[] operaciones, int[] valores) {
		this.val = "" + valor;
		this.izq = null;
		this.der = null;
		this.profundidad = 1;
		this.ops = operaciones;
		this.vals = valores;
	}
	
	//constructor random
	public AST(int prof, int[] vals, String[] operaciones, boolean root) {
		this.ops = operaciones;
		this.vals = vals;
		if(root) {
			this.profundidad = ThreadLocalRandom.current().nextInt(1, prof + 1);
		}
		else {
			this.profundidad = prof;
		}
		if(this.profundidad == 1) {
			int v = vals[ThreadLocalRandom.current().nextInt(0, vals.length)];
			this.val = "" + v;
			this.izq = null;
			this.der = null;
		}
		else {
			this.val = ops[ThreadLocalRandom.current().nextInt(0, ops.length)];
			int moneda = ThreadLocalRandom.current().nextInt(0, 2);
			if(moneda == 0) {
				AST izq = new AST(this.profundidad - 1, vals, ops, false);
				int prof2 = ThreadLocalRandom.current().nextInt(1, this.profundidad);
				AST der = new AST(prof2, vals, ops, false);
				this.izq = izq;
				this.der = der;
			}
			else {
				AST der = new AST(this.profundidad - 1, vals, ops, false);
				int prof2 = ThreadLocalRandom.current().nextInt(1, this.profundidad);
				AST izq = new AST(prof2, vals, ops, false);
				this.izq = izq;
				this.der = der;
			}
		}
	}
	
	//cruzar con otro ast, promediandose
	public AST cruzar(AST mate) {
		AST suma = new AST("+", this, mate, this.ops, this.vals);
		AST dos = new AST(2, this.ops, this.vals);
		AST promedio = new AST("/", suma, dos, this.ops, this.vals);
		return promedio;
	}
	
	//mutar
	public AST mutar(double mr) {
		Random generator = new Random();
		double number = generator.nextDouble();
		double prob = number / this.profundidad;
		if(prob > mr) {
			if(this.profundidad == 1) {
				int indice = ThreadLocalRandom.current().nextInt(0, this.vals.length);
				AST hojaNueva = new AST(this.vals[indice], this.ops, this.vals);
				return hojaNueva;
			}
			else {
				int indice = ThreadLocalRandom.current().nextInt(0, this.vals.length);
				AST astNuevo = new AST(this.profundidad, this.vals, this.ops, true);
				return astNuevo;
			}
		}
		else {
			if(this.profundidad > 1) {
				AST astNuevo = this.copy();
				astNuevo.izq = this.izq.mutar(mr);
				astNuevo.der = this.der.mutar(mr);
				return astNuevo;
			}
			else {
				return this;
			}
		}
	}
	
	//copiar ast
	public AST copy() {
		AST copia = new AST(this.val, null, null, this.ops, this.vals);
		if(this.izq != null) {
			AST copiaIzq = this.izq.copy();
			copia.izq = copiaIzq;
		}
		if(this.der != null) {
			AST copiaDer = this.der.copy();
			copia.der = copiaDer;
		}
		return copia;
	}
	
	//evaluar ast
	public int evaluar() {
		if(this.izq == null && this.der == null) {
			return Integer.valueOf(this.val);
		}
		else {
			switch(this.val) {
				case "+":
					return this.izq.evaluar() + this.der.evaluar();
				case "-":
					return this.izq.evaluar() - this.der.evaluar();
				case "*":
					return this.izq.evaluar() * this.der.evaluar();
				case "/":
					return this.izq.evaluar() / this.der.evaluar();
				case "%":
					return this.izq.evaluar() % this.der.evaluar();
				default:
					System.out.println("operador invalido");
					return 0;
			}
		}
	}
	
}
