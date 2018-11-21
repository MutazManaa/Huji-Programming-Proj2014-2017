import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

public class SimpleSetPerformanceAnalyzer {

	public static void main(String args[]) {

		SimpleSet[] simpleSet1 = new SimpleSet[5];
		SimpleSet[] simpleSet2 = new SimpleSet[5];
		
		//System.out.println("vgdcxvvcvcxvcvcxv");

		simpleSet1[0] = new CollectionFacadeSet(new TreeSet<String>());
		simpleSet1[1] = new CollectionFacadeSet(new LinkedList<String>());
		simpleSet1[2] = new CollectionFacadeSet(new HashSet<String>());
		simpleSet1[3] = new OpenHashSet();
		simpleSet1[4] = new ChainedHashSet();

		simpleSet2[0] = new CollectionFacadeSet(new TreeSet<String>());
		simpleSet2[1] = new CollectionFacadeSet(new LinkedList<String>());
		simpleSet2[2] = new CollectionFacadeSet(new HashSet<String>());
		simpleSet2[3] = new OpenHashSet();
		simpleSet2[4] = new ChainedHashSet();

		String[] data1 = Ex4Utils.file2array("data1.txt");
		String[] data2 = Ex4Utils.file2array("data2.txt");

		long[] addTime1 = new long[5];
		long[] addTime2 = new long[5];
		long[] hicontains1Time = new long[5];
		long[] hicontains2Time = new long[5];
		long[] numcontains1Time = new long[5];
		long[] numcontains2Time = new long[5];

		for (int i = 0; i < 5; i++) {
			long timeBefore1 = new Date().getTime();

			for (String key : data1) {

				simpleSet1[i].add(key);

			}
			long timeAfter1 = new Date().getTime();
			addTime1[i] = timeAfter1 - timeBefore1;

			long timeBefore2 = new Date().getTime();

			for (String key : data2) {

				simpleSet2[i].add(key);

			}

			long timeAfter2 = new Date().getTime();
			addTime2[i] = timeAfter2 - timeBefore2;

		}

		for (int i = 0; i < 5; i++) {
			long timeBefore1 = new Date().getTime();
			simpleSet1[i].contains("hi");
			long timeAfter1 = new Date().getTime();
			hicontains1Time[i] = timeAfter1 - timeBefore1;

			// -----------------------------------------------------
			long timeBefore2 = new Date().getTime();
			simpleSet2[i].contains("hi");
			long timeAfter2 = new Date().getTime();
			hicontains2Time[i] = timeAfter2 - timeBefore2;

			// -----------------------------------------------------
			long timeBefore3 = new Date().getTime();
			simpleSet1[i].contains("-13170890158");
			long timeAfter3 = new Date().getTime();
			numcontains1Time[i] = timeAfter3 - timeBefore3;
			// ------------------------------------------------------
			long timeBefore4 = new Date().getTime();
			simpleSet2[i].contains("23");
			long timeAfter4 = new Date().getTime();
			numcontains2Time[i] = timeAfter4 - timeBefore4;
		}
			// ---------------------------------------------------------

			
			int min1 = minVal(addTime1);
			int min2 = minVal(addTime2);
			int min3 = minVal(hicontains1Time);
			int min4 = minVal(hicontains2Time);
			int min5 = minVal(numcontains1Time);
			int min6 = minVal(numcontains2Time);
		
			System.out.println("data1: The best data structure of addional method is:" +simpleSet1[min1].toString() + "with Time:" +addTime1[min1]);
			System.out.println("data2: The best data structure of addional method is:" +simpleSet2[min2].toString() + "with Time:" +addTime2[min2]);
			System.out.println("data1: The best data structure of contains method is:" +simpleSet1[min3].toString() + "with Time:" +hicontains1Time[min3]);
			System.out.println("data2: The best data structure of contains method is:" +simpleSet2[min4].toString() + "with Time:" +hicontains2Time[min4]);
			System.out.println("data1: The best data structure of contains method is:" +simpleSet1[min5].toString() + "with Time:" +numcontains1Time[min5]);
			System.out.println("data2: The best data structure of contains method is:" +simpleSet2[min6].toString() + "with Time:" +numcontains2Time[min6]);
			
		
		
		
		
	}
		
		
		//-------------------help method-------
		private static int  minVal(long[] Times) {
			long min = Times[0];
			int ktr;
			for ( ktr = 0; ktr < Times.length; ktr++) {
				if (Times[ktr] < min) {
					min = Times[ktr];
				}
			}
			return ktr;
		}
	

	}


