import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.RandomAccess;
import java.util.stream.Stream;


public class RandProductSearch extends JFrame {
    JPanel mainPnl, topPnl, centerPnl, bottomPnl;
    JButton exitBtn, searchBtn,clearBtn;
    JLabel productNameLbl;
    JScrollPane scroller;
    JTextField productNameTF;
    JTextArea resultTA;
    ArrayList<String> rec = new ArrayList<>();

    RandomAccessFile randomAccessFile;
    ArrayList<Product> products = new ArrayList<>();

    public static void main(String[] args) {
        RandProductSearch frame = new RandProductSearch();
    }
    public RandProductSearch()
    {
        Toolkit kit = Toolkit.getDefaultToolkit();
        Dimension screensize = kit.getScreenSize();

        int width = screensize.width;
        int height = screensize.height;
        setSize(width/3, height/2);
        setLocation(width/3,height/6);
        setTitle("Random Product Search");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        createGUI();
        setVisible(true);
    }
    private void createGUI()
    {
        mainPnl = new JPanel();

        createTopPnl();
        createCenterPnl();
        createBottomPnl();

        mainPnl.setLayout(new BorderLayout());
        mainPnl.add(topPnl,BorderLayout.NORTH);
        mainPnl.add(centerPnl);
        mainPnl.add(bottomPnl,BorderLayout.SOUTH);

        add(mainPnl);
    }
    private void createTopPnl()
    {
        topPnl = new JPanel();
        topPnl.setBorder(new TitledBorder(new LineBorder(Color.BLACK,1)));
        productNameLbl = new JLabel("Product name: ");
        productNameTF = new JTextField(20);
        productNameTF.setBorder(null);

        topPnl.add(productNameLbl);
        topPnl.add(productNameTF);
    }
    private void createCenterPnl() {
        centerPnl = new JPanel();
        centerPnl.setBorder(new TitledBorder(new LineBorder(Color.BLACK,1),"Product Found"));

        resultTA = new JTextArea(12,30);
        resultTA.setEditable(false);
        scroller = new JScrollPane(resultTA);
        centerPnl.add(scroller);
    }
    private void createBottomPnl()
    {
        bottomPnl = new JPanel();
        searchBtn = new JButton("Search");
        searchBtn.setFont(new Font("Arial",Font.BOLD,14));
        searchBtn.addActionListener(e ->
        {
            resultTA.setText("");
            StringBuilder res = new StringBuilder(productNameTF.getText());
            if(res.length()==0)
            {
                JOptionPane.showMessageDialog(null, "Please enter product name!", "ERROR",JOptionPane.ERROR_MESSAGE);
            }
            else
            {
                try
                {
                    RandomAccessFile randomAccessFile = new RandomAccessFile("src/ProductRecord.txt","r");
                    while(randomAccessFile.getFilePointer() < randomAccessFile.length())
                    {
                        rec.add(randomAccessFile.readLine());
                    }
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null,"Couldn't find any file", "ERROR",JOptionPane.ERROR_MESSAGE);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
            Stream<String> stream = rec.stream().filter(s ->s.contains(res));
            stream.forEach(s -> resultTA.append(s));
        });

        clearBtn=new JButton("Clear");
        clearBtn.setFont(new Font("Arial",Font.BOLD,14));
        clearBtn.addActionListener(e -> {
            clear();
        });

        exitBtn = new JButton("Exit");
        exitBtn.setFont(new Font("Arial",Font.BOLD,14));
        exitBtn.addActionListener(e -> {
            System.exit(0);
        });
        bottomPnl.add(searchBtn);
        bottomPnl.add(clearBtn);
        bottomPnl.add(exitBtn);
    }
    private void clear()
    {
        resultTA.setText("");
        productNameTF.setText("");
    }

}
