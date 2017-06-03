import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Login
{

	private String username = "username";
	private String password = "password";

	public Login()
	{
		try
		{
			String html = loginAndGetHTML();
			String zahl = parseHTML(html);
			System.out.println(zahl);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public String loginAndGetHTML() throws Exception
	{
		CloseableHttpClient httpclient = HttpClients.createDefault();
		String html = "";

		HttpPost httpPost = new HttpPost("https://login.rz.ruhr-uni-bochum.de/cgi-bin/laklogin");
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("code", "1&"));
		nvps.add(new BasicNameValuePair("loginid", password)); // USERNAME
		nvps.add(new BasicNameValuePair("password", username)); // PASSWORD
		nvps.add(new BasicNameValuePair("ipaddr", ""));
		nvps.add(new BasicNameValuePair("action", "Login"));

		httpPost.setEntity(new UrlEncodedFormEntity(nvps));
		httpPost.addHeader("Referer", "https://login.rz.ruhr-uni-bochum.de/cgi-bin/laklogin");
		httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:49.0) Gecko/20100101 Firefox/49.0");
		CloseableHttpResponse response = httpclient.execute(httpPost);

		try
		{
			HttpEntity entity = response.getEntity();
			html = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		}
		finally
		{
			response.close();
		}

		if(html.contains("Authentisierung fehlgeschlagen"))
		{
			throw new Exception("Login Fehlgeschlagen");
		}

		return html;
	}

	public String parseHTML(String html) throws Exception
	{
		Document doc = Jsoup.parse(html);
		String zahl = doc.text();
		return zahl;
	}

	public static void main(String[] args)
	{
		new Login();
	}

}
