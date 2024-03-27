import java.io.File;
import java.util.ArrayList;

public class run {

	public static void main(String[] args) throws Exception {
		KaggleApiDownloader api = new KaggleApiDownloader();
		api.Api("jp797498e/twitter-entity-sentiment-analysis");
		CSVToTextConverter convertor = new CSVToTextConverter();
		convertor.convert("twitter_validation.csv", "twitter_validation.txt", 4);
		api.Api("nicapotato/womens-ecommerce-clothing-reviews");
		convertor.convert("Womens Clothing E-Commerce Reviews.csv", "womens.txt", 4);
		// kaggle datasets download -d nicapotato/womens-ecommerce-clothing-reviews
		// kaggle datasets download -d nelgiriyewithana/mcdonalds-store-reviews
		api.Api("nelgiriyewithana/mcdonalds-store-reviews");
		convertor.convert("McDonald_s_Reviews.csv", "mcDonald.txt", 9);

		RandomSelector randomselect = new RandomSelector();
		randomselect.randomselect();
		Preprocessing p = new Preprocessing();

		File pos = new File("positiveTraining.txt");
		File neg = new File("negativeTraining.txt");
		File neutral = new File("neutralTraining.txt");
		ArrayList<ArrayList<String>> positiveList = p.readFile(pos);
		ArrayList<ArrayList<String>> negativeList = p.readFile(neg);
		ArrayList<ArrayList<String>> neutralList = p.readFile(neutral);

		p.positiveList = positiveList;
		p.negativeList = negativeList;
		p.neutralList = neutralList;
		p.tfidf(positiveList, 1);
		p.tfidf(negativeList, -1);
		p.tfidf(neutralList, 0);
		// System.out.println("**********************");
		// System.out.println(positiveList);
		// System.out.println("*************************");
		p.loadFile();
		Bayes bayes = new Bayes();
		bayes.loadHashMap();
		svm lr = new svm();
		lr.loadHashMap();
		runPreprocess run = new runPreprocess();
		run.testing(bayes, p, "convertedtotxt.txt");
		// run.testing(lr, p);
		// run.testing(bayes, p, "womens.txt");
		// run.testing(bayes, p, "mcDonald.txt");
	}

}
