import javax.swing.JOptionPane;

public class Main {

	public static void main(String[] args) throws CloneNotSupportedException {
		Hex j1=new Hex();
		Hex j2 = null;
		try {
			j2=(Hex) j1.clone();
		}
		catch(CloneNotSupportedException e) {
			JOptionPane.showMessageDialog(null,e);
		}
			j1.Play();
			//j2.Play();
	}

}
