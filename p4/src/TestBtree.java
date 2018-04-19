/**
 * 
 * @author Matthew Castrigno
 *
 */
public class TestBtree {

	public static void main(String[] args) {
		
		//Test Case # 1 insert 3 objects in a tree of degree 2
		
		long A,B,C, D, E, F, G, H, I, J, K, L, M, N;
		A = 14;
		B = 13;
		C = 12;
		D = 11;
		E = 10;
		F = 9;
		G = 8;
		H = 7;
		I = 6;
		J = 5;
		K = 4;
		L = 3;
		M = 2;
		N = 1;
		
		int degree = 2;
		int sequenceLength = 2;
		TreeObject ObjectA, ObjectB, ObjectC, ObjectD, ObjectE, ObjectF, ObjectG, ObjectH, 
				   ObjectI, ObjectJ, ObjectK, ObjectL, ObjectM, ObjectN;
		String testFilename = "testFilename";
		
		ObjectA = new TreeObject(A);
		ObjectB = new TreeObject(B);
		ObjectC = new TreeObject(C);
		ObjectD = new TreeObject(D);
		ObjectE = new TreeObject(E);
		ObjectF = new TreeObject(F);
		ObjectG = new TreeObject(G);
		ObjectH = new TreeObject(H);
		ObjectI = new TreeObject(I);
		ObjectJ = new TreeObject(J);
		ObjectK = new TreeObject(K);
		ObjectL = new TreeObject(L);
		ObjectM = new TreeObject(M);
		ObjectN = new TreeObject(N);
		
		BTree testTree1 = new BTree(degree, sequenceLength, testFilename);
		
		testTree1.insert(ObjectA); 
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}			
		System.out.println("----------------------------------------------------\n");
		
		testTree1.insert(ObjectB);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}	
		System.out.println("----------------------------------------------------\n");		
		
		testTree1.insert(ObjectC);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");
		
		testTree1.insert(ObjectD);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");

		testTree1.insert(ObjectE);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");		
		
		testTree1.insert(ObjectF);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");
	
		
		testTree1.insert(ObjectG);		
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");
		
		
		testTree1.insert(ObjectH);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");
				
		testTree1.insert(ObjectI);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");
		
		testTree1.insert(ObjectJ);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");
		
		testTree1.insert(ObjectK);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");		
		
		testTree1.insert(ObjectL);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");	
		
		testTree1.insert(ObjectM);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");	
		
		testTree1.insert(ObjectN);
		for(int i = 1; i <= testTree1.getNumOfTreeNodes(); i++) {
			System.out.println(testTree1.storage.nodeRead(i).toString());
		}
		System.out.println("----------------------------------------------------\n");	
		
		System.out.println(testTree1.toString());
	}

}
