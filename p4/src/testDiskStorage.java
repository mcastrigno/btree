/**
 * 
 * @author Matthew Castrigno
 * 
 */
public class testDiskStorage {
	public static void main(String[] args) {
		long A,B,C, D, E, F, G, H, I, J, K, L, M, N;
		A = 8;
		B = 1;
		C = 5;
		D = 2;
		E = 6;
		F = 3;
		G = 3;
		H = 4;
		I = 9;
		J = 10;
		K = 11;
		L = 12;
		M = 13;
		N = 14;
		
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
		testTree1.insert(ObjectB); 
		testTree1.insert(ObjectC); 
		System.out.println(testTree1.toString());
		testTree1.insert(ObjectD); 
		System.out.println(testTree1.toString());
		testTree1.rootWrite();
		testTree1.closeTreeFile();
	}
}