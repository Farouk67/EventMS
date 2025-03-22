package weka2;

import java.util.Random;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
public class App
{
     public static void main( String[] args ) {
         try {
             // get the training & test set
             DataSource source = new DataSource("src\\main\\data\\bmw-training.arff");
             Instances dataset = source.getDataSet();
             dataset.setClassIndex(dataset.numAttributes()-1);
             DataSource test = new DataSource("src\\main\\data\\bmw-test.arff");
             Instances testDset = test.getDataSet();
            testDset.setClassIndex(testDset.numAttributes()-1);
             J48 classifier = new J48(); // build the classifier
             classifier.buildClassifier(dataset);
            Evaluation eval = new Evaluation(dataset); // Create an evaluation object
             eval.evaluateModel(classifier, dataset);  // evaluate using training set
            eval.crossValidateModel(classifier, dataset, 10, new Random(1)); // evaluate using cross validation
             eval.evaluateModel(classifier, testDset); // Evaluate model using test dataset
             System.out.println(eval.toSummaryString());
            // Create an unseen instance
            // set all attributes except the class attribute 
             double[] values = new double[dataset.numAttributes()];
            values[0] = 5.1; // Example value for attribute 1
             values[1] = 3.5; // Example value for attribute 2
           values[2] = 1.4; // Example value for attribute 3
             // Create the instance and set its dataset reference
             Instance unseenInstance = new DenseInstance(1.0, values);
             unseenInstance.setDataset(dataset);
            double res = classifier.classifyInstance(unseenInstance);
            System.out.println("Classified label: " + res);
            }
           catch(Exception e)
            {
             System.out.println(e.getMessage());
           }
             }
            }
            
