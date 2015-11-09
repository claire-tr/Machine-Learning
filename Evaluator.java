
public class Evaluator {
	Classifier classifier;
	TrainTestSets tts;
Evaluator(Classifier c, TrainTestSets tts)
{
	this.classifier = c;
	this.tts = tts;
}

Performance holdOut() throws Exception
{
	classifier.train(tts.train);
	Performance p = classifier.classify(tts.test);
	System.out.println("Accuracy: "+p.getStringAccuracy());
	return p;
}
Performance kFold(int k) throws Exception
{
	Performance all = new Performance(k);
	for(int i=0;i<k;i++)
	{
		TrainTestSets temp = tts.train.getCVSets(i,k);
		classifier.train(temp.train);
		//System.out.println(temp.test.examples);
		Performance p = classifier.classify(temp.test);
		all.addAccuracy(p.getDoubleAccuracy());
	}
	System.out.println("Accuracy: "+all.getStringMeanAccuracy());
	System.out.println("Variance: "+all.getVariace());
	return all;
}
}
