import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.math.*;


public class  Navie_bayes_Classifier extends JFrame implements ActionListener {
 
//UI Elements	
	JLabel Input,Output;
 	JButton Button;
 	JTextField In,Out;
 
//HashMap for Storing DataSet	
	Map<String,Integer> Anger=new HashMap<String,Integer>();
	Map<String,Integer> Joy=new HashMap<String,Integer>();
	Map<String,Integer> Love=new HashMap<String,Integer>();
	Map<String,Integer> Sad=new HashMap<String,Integer>();
	Map<String,Integer> Suprise=new HashMap<String,Integer>();


 	Navie_bayes_Classifier throws IOException ()
 	{
 		Input=new JLabel("INPUT TEXT");
  
  		Output=new JLabel("Sentiment Predicted");
  		Button=new JButton("Predict");
 
  		In=new JTextField(60);
  		Out=new JTextField(10);
		
		String [] Emotions={"Anger","Joy","Love","Sad","Suprise"};
		
		//Loading Dataset
		for(int i=0;i<Arrays.asList(Emotions).size();i++)
		{

		        BufferedReader br = new BufferedReader(new FileReader("/home/akash/Desktop/Tweets/Dataset/"+Emotions[i]));//Path to Dataset
			String feature;

			while ((feature= br.readLine()) != null) {

				String val[]=feature.split(",");
				if(i==0)
					Anger.put(val[0].replaceAll(" +", " "),Integer.parseInt(val[1]));	
				if(i==1)
					Joy.put(val[0].replaceAll(" +", " "),Integer.parseInt(val[1]));	
				if(i==2)
					Love.put(val[0].replaceAll(" +", " "),Integer.parseInt(val[1]));	
				if(i==3)
					Sad.put(val[0].replaceAll(" +", " "),Integer.parseInt(val[1]));	
				if(i==4)
					Suprise.put(val[0].replaceAll(" +", " "),Integer.parseInt(val[1]));	
			}
		}


		//Add all the elements
		add(Input);
  		add(In);
  		add(Output);
  		add(Out);
  		add(Button);
  
 		Button.addActionListener(this);
  		setSize(700,200);
  		setLayout(new FlowLayout());
  		setTitle("Sentiment Predictor");
 	}

        //OnClick Function for the button
 	public void actionPerformed(ActionEvent ae)
 	{
  		String input;
		String [] Emotions={"Anger","Joy","Love","Sad","Suprise"};

  		if(ae.getSource()==Button)
  		{
  			input=In.getText().toString();

                        //Formatting the testing data for classification
			input =input.replaceAll("@\\w+", "at_user");//replace @username to at_user
			input=input.replaceAll("[ ]\\W+"," ");//Replace all non alpha-numeric Characters
			input=input.replaceAll(" +", " ");//Remove extra whitespaces
			input=input.replaceAll("#", "");//Remove hashtags
			input=input.replaceAll("https://.*","");//Remove urls
			input=input.replaceAll("[^\\w||^\\s]"," ");//Remove all puncuations  marks

			//Declaring Parameters need for Classification			
			String words[]=input.split(" ");
			int words_no=Arrays.asList(words).size();
			double[][] probability =new double[words_no][6];
			double[] inter_probs=new double[6];
		 	BigDecimal[] final_probs=new BigDecimal[6];


			for(int i=0;i<words_no;i++)
				for(int j=0;j<5;j++)
				{
					probability[i][j]=0.333;
                               		final_probs[j]=new BigDecimal(1.0);

			        }

			int curr=0,present=0;
			double probs_sum=0.0;

                        //Naives Bayes Classifier			
			for(String word:words)
			{
				if(Anger.containsKey(word))
				{
					double times=Anger.get(word);
					double total=Anger.size();
					inter_probs[0]=times/total;
				}
				else
					inter_probs[0]=0;
			
				if(Joy.containsKey(word))
				{
					double times=Joy.get(word);
					double total=Joy.size();
					inter_probs[1]=times/total;
				}
				else
					inter_probs[1]=0;

			
				if(Love.containsKey(word))
				{
					double times=Love.get(word);
					double total=Love.size();
					inter_probs[2]=times/total;
				}
				else
					inter_probs[2]=0;
				
				if(Suprise.containsKey(word))
				{
					double times=Suprise.get(word);
					double total=Suprise.size();
					inter_probs[4]=times/total;
				}
				else
					inter_probs[4]=0;
				if(Sad.containsKey(word))
				{ 
					double times=Sad.get(word);
					double total=Sad.size();
					inter_probs[3]=times/total;
				}
				else
					inter_probs[3]=0;	
			
				for(int i=0;i<5;i++)
				{
					if(inter_probs[i]>0)
					{
						probability[curr][i]=inter_probs[i];
						probs_sum+=inter_probs[i];
						present++;
                                 
					}
				}
				double rem_sum=1-probs_sum;
			
				if(present>0)
				{  
					for(int j=0;j<5;j++)
					{
						if(probability[curr][j]==0.333)
							probability[curr][j]=0.000001*rem_sum;
						
						System.out.println(word+","+Emotions[j]+": "+probability[curr][j]);			
					} 
  				}	
 

				curr++;
				probs_sum=0.0;
				present=0;
			}


			int emo=-1;
			BigDecimal large=new BigDecimal(-34.56);
			MathContext mc=new MathContext(20);

			for(int j=0;j<5;j++)
				for(int i=0;i<words_no;i++)
					final_probs[j]=final_probs[j].multiply(new BigDecimal(probability[i][j]),mc);
			for(int i=0;i<5;i++)
			{  
				System.out.println(final_probs[i]);	
				if(large.compareTo(final_probs[i])==-1)
				{
					large=final_probs[i];
					emo=i;
				}				
			}

  			Out.setText(String.valueOf(Emotions[emo]));
  		}
  
	}

 	public static void main(String args[])
 	{
  		Navie_bayes_Classifier classifier=new Navie_bayes_Classifier();
  		classifier.setVisible(true);
  		classifier.setLocation(700,200);
  	}

}

