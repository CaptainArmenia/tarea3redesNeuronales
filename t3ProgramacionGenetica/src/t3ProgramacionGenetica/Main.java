package t3ProgramacionGenetica;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] vals = {1, 2, 3};
		String[] ops = {"+", "-", "*"};
		
		/*
		AST ast = new AST(3, vals, ops, true);
		AST ast2 = new AST(3, vals, ops, true);
		BTreePrinter.printNode(ast);
		BTreePrinter.printNode(ast.mutar(0.1));
		
		System.out.println(ast.evaluar());
		BTreePrinter.printNode(ast2);
		AST cruza = ast.cruzar(ast2);
		BTreePrinter.printNode(cruza);
		System.out.println(cruza.evaluar());
		*/
		
		//Aproximar(numero objetivo, terminales, operaciones, tamaño de las generaciones, tasa de mutacion)
		Aproximar t1 = new Aproximar(100, vals, ops, 50, 0.1);
		t1.ejecutar();
		
		
	}

}
