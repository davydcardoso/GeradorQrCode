package Hiperion;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;

import Models.ResponseQrCode;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.HashMap;
import javax.swing.JTextPane;


public class homeFrame extends JFrame {

	private JPanel contentPane;
	private JTextField edtToken;
	private JTextField edtUrlApi;
	private JPanel panel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					homeFrame frame = new homeFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public homeFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 769, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("Gerar Qr Code");
		btnNewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				gerarQrCode();
			}
		});
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnNewButton.setBounds(615, 407, 128, 23);
		contentPane.add(btnNewButton);
		
		edtToken = new JTextField();
		edtToken.setBounds(10, 410, 595, 20);
		contentPane.add(edtToken);
		edtToken.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Token");
		lblNewLabel.setBounds(10, 396, 98, 14);
		contentPane.add(lblNewLabel);
		
		edtUrlApi = new JTextField();
		edtUrlApi.setBounds(10, 373, 595, 20);
		edtUrlApi.setText("http://localhost:3001/api/v1/whatsapp/qrcode/image");
		contentPane.add(edtUrlApi);
		edtUrlApi.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Url API");
		lblNewLabel_1.setBounds(10, 359, 98, 14);
		contentPane.add(lblNewLabel_1);
		
		panel = new JPanel();
		panel.setBounds(134, 11, 333, 333);
		contentPane.add(panel);
	}
	
	private void gerarQrCode() {
		try {
			String urlApi = edtUrlApi.getText();
			String token = edtToken.getText();
			
			URL url = new URL(urlApi.toString());
			
			URLConnection con = url.openConnection();
			HttpURLConnection http = (HttpURLConnection)con;
			
			http.setRequestMethod("GET");
			http.setRequestProperty("Authorization", "Bearer "+token);
			http.setRequestProperty("Content-Type","application/json");
			
			BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
			
			String inputLine;
			
			StringBuffer response = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			
	        in.close();
	        
	        
	        Gson gson = new Gson();
	        
	        ResponseQrCode json = gson.fromJson(response.toString(), ResponseQrCode.class);
	        
	        BufferedImage image = null;
	        
	        byte[] imageByte;
	        
	        
	        imageByte = Base64.getDecoder().decode(json.qrcode);    
	        
	        ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
	        image = ImageIO.read(bis);
	        
	        bis.close();
	        
	        ImageIO.write(image, "png", new File("./qrcode.png"));
	        
	        JFrame frame = new JFrame("Qrcode");    
			JPanel panel=new JPanel();  
			panel.setLayout(new FlowLayout());      
			BufferedImage myPicture = ImageIO.read(new File("./qrcode.png"));
			JLabel picLabel = new JLabel(new ImageIcon(myPicture));
			panel.add(picLabel);
			frame.add(panel);
			frame.setSize(350,350); 
			frame.setVisible(true); 
	        
	        
//	        JOptionPane.showMessageDialog(null, json.qrcode);
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}





;