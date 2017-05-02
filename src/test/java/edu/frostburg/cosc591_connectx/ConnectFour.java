



public class ConnectFour
{
public static String[][] createPattern()
{
 
   String[][] f = new String[7][15];
  
  for (int i =0;i<f.length;i++)
  {
    
     for (int j =0;j<f[i].length;j++)
    {
   
      if (j% 2 == 0) f[i][j] ="|";
      else f[i][j] = " ";
      
      if (i==6) f[i][j]= "-";
    }
    
  }
  return f;
}

public static void printPattern(String[][] f)
{
  for (int i =0;i<f.length;i++)
  {
    for (int j=0;j<f[i].length;j++)
    {
      System.out.print(f[i][j]);
    }
    System.out.println();
  }
}



public static void main (String[] args)
{

  String[][] f = createPattern();

  boolean loop = true;
  int count = 0;
  printPattern(f);
 
}
}
