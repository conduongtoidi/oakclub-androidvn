package com.oakclub.android.net;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URI;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.facebook.Session;
import com.oakclub.android.MainActivity;
import com.oakclub.android.model.ChatHistoryReturnObject;
import com.oakclub.android.model.GetAccountSettingsReturnObject;
import com.oakclub.android.model.GetConfigData;
import com.oakclub.android.model.GetDataLanguageReturnObject;
import com.oakclub.android.model.GetSnapShot;
import com.oakclub.android.model.GetSnapshotSettingsReturnObject;
import com.oakclub.android.model.HangoutProfileOtherReturnObject;
import com.oakclub.android.model.HangoutProfileReturnObject;
import com.oakclub.android.model.ListChatReturnObject;
import com.oakclub.android.model.ListPhotoReturnObject;
import com.oakclub.android.model.PostMethodReturnObject;
import com.oakclub.android.model.ProfileUpdateFirstTimeObject;
import com.oakclub.android.model.SenVIPRegisterReturnObject;
import com.oakclub.android.model.SendChatReturnObject;
import com.oakclub.android.model.SendMessageReturnObject;
import com.oakclub.android.model.SendRegisterReturnObject;
import com.oakclub.android.model.SetLikeMessageReturnObject;
import com.oakclub.android.model.SetLocationReturnObject;
import com.oakclub.android.model.SetViewMutualReturnObject;
import com.oakclub.android.model.SettingReturnObject;
import com.oakclub.android.model.UploadPhotoReturnObject;
import com.oakclub.android.model.UploadVideoObject;
import com.oakclub.android.model.VerifiedReturnObject;
import com.oakclub.android.util.Constants;

public class OakClubApi extends ApiConnect implements IOakClubApi {
	private static IOakClubApi sMock;
	private static final int CONNECTION_TIMEOUT = 20000;
	private static final int SOCKET_TIMEOUT = 60000;
	
	private String baseUrl;

	public OakClubApi(Context context) {
		super(context);
	}

	public OakClubApi(Context context, String baseUrl) {
		super(context);
		this.baseUrl = baseUrl;
	}

	public static IOakClubApi createInstance(Context context, String baseUrl) {
		if (sMock != null) {
			return sMock;
		} else {
			return new OakClubApi(context, baseUrl);
		}
	}

	/**
	 * Connect to http via GET method
	 * 
	 * @param url
	 * @param paramList
	 * @return
	 */
	public String excuteGet(String url, List<NameValuePair> paramList) {
		if (null != paramList) {
			url += getStrParam(paramList);
		}
		HttpClient hClient = new DefaultHttpClient();
//		hClient = sslClient(hClient);
		HttpConnectionParams.setConnectionTimeout(hClient.getParams(),
				CONNECTION_TIMEOUT); // Timeout
		HttpConnectionParams.setSoTimeout(hClient.getParams(), SOCKET_TIMEOUT);
		try {
			HttpGet hget = new HttpGet();
			String headerValue = "UsernameToken "
								+"Username=\""+ MainActivity.facebook_user_id
								+"\", AccessToken=\""+MainActivity.access_token
								+"\", Nonce=\"1ifn7s\", Created=\"2013-10-19T07:12:43.407Z\"";
			hget.setHeader(Constants.HEADER_X_WSSE,headerValue);
			hget.setHeader(Constants.HEADER_ACCEPT,"application/json");
			hget.setHeader(Constants.HEADER_ACCEPT,"text/html");
			hget.setHeader(Constants.HTTP_USER_AGENT, "Android");
			hget.setURI(new URI(url));
			HttpResponse response = hClient.execute(hget);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				
				String responseString = out.toString();
				out.close();
				return responseString;
			} else {
				return "";
			}
		} catch (ConnectTimeoutException e) {

			return "";

		} catch (Exception e) {
			return "";

		}
	}

	/**
	 * Post method
	 * 
	 * @param paramList
	 *            : Danh sach va tham so dang NameValuePair
	 * 
	 * @return JSON
	 */
	public String excutePost(String url, List<NameValuePair> paramList) {
		HttpClient httpclient = new DefaultHttpClient();
		httpclient = sslClient(httpclient);
		HttpConnectionParams.setConnectionTimeout(httpclient.getParams(),
				CONNECTION_TIMEOUT); // Timeout
		HttpConnectionParams.setSoTimeout(httpclient.getParams(),
				SOCKET_TIMEOUT);
		HttpPost httppost = new HttpPost(url);

		HttpResponse response = null;
		try {
			String headerValue = "UsernameToken "
					+"Username=\""+ MainActivity.facebook_user_id
					+"\", AccessToken=\""+MainActivity.access_token

					+"\", Nonce=\"1ifn7s\", Created=\"2013-10-19T07:12:43.407Z\"";
			httppost.setHeader(Constants.HEADER_X_WSSE,headerValue);
			httppost.setHeader(Constants.HEADER_ACCEPT,"application/json");
			httppost.setHeader(Constants.HEADER_ACCEPT,"text/html");
			httppost.setHeader(Constants.HTTP_USER_AGENT, "Android");
			httppost.setEntity(new UrlEncodedFormEntity(paramList, "UTF-8"));
			response = httpclient.execute(httppost);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			if (HttpStatus.SC_NO_CONTENT == response.getStatusLine()
					.getStatusCode()) {
				return "";
			} else if (HttpStatus.SC_OK == response.getStatusLine()
					.getStatusCode()) {
				response.getEntity().writeTo(byteArrayOutputStream);
				String ret = byteArrayOutputStream.toString();
				return ret;
			} else {
				return "";
			}

		} catch (ConnectTimeoutException e) {
			return "";
		} catch (Exception e) {
			return "";
		}
	}
	public String execPostImg(String url, MultipartEntity mulEntity) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpConnectionParams
				.setConnectionTimeout(httpclient.getParams(), CONNECTION_TIMEOUT); 
		HttpConnectionParams.setSoTimeout(httpclient.getParams(), SOCKET_TIMEOUT);
		HttpPost httppost = new HttpPost(url);
		try {
			httppost.setEntity(mulEntity);
			String headerValue = "UsernameToken "
					+"Username=\""+ MainActivity.facebook_user_id
					+"\", AccessToken=\""+MainActivity.access_token

					+"\", Nonce=\"1ifn7s\", Created=\"2013-10-19T07:12:43.407Z\"";
			httppost.setHeader(Constants.HEADER_X_WSSE,headerValue);
			httppost.setHeader(Constants.HEADER_ACCEPT,"application/json");
			httppost.setHeader(Constants.HEADER_ACCEPT,"text/html");
			HttpResponse response = httpclient.execute(httppost);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			response.getEntity().writeTo(byteArrayOutputStream);

			if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
				String ret = byteArrayOutputStream.toString();
				return ret;
			} else {
				return "";
			}

		}  catch (Exception e) {
			return null;

		}
	}
	private HttpClient sslClient(HttpClient client) {
		try {
			X509TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] xcs,
						String string) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};
			SSLContext ctx = SSLContext.getInstance("TLS");
			ctx.init(null, new TrustManager[] { tm }, null);
			SSLSocketFactory ssf = new MySSLSocketFactory(ctx);
			ssf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			ClientConnectionManager ccm = client.getConnectionManager();
			SchemeRegistry sr = ccm.getSchemeRegistry();
			sr.register(new Scheme("https", ssf, 443));
			sr.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			return new DefaultHttpClient(ccm, client.getParams());
		} catch (Exception ex) {
			return null;
		}
	}

	public class MySSLSocketFactory extends SSLSocketFactory {
		SSLContext sslContext = SSLContext.getInstance("TLS");

		public MySSLSocketFactory(KeyStore truststore)
				throws NoSuchAlgorithmException, KeyManagementException,
				KeyStoreException, UnrecoverableKeyException {
			super(truststore);

			TrustManager tm = new X509TrustManager() {
				public void checkClientTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public void checkServerTrusted(X509Certificate[] chain,
						String authType) throws CertificateException {
				}

				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
			};

			sslContext.init(null, new TrustManager[] { tm }, null);
		}

		public MySSLSocketFactory(SSLContext context)
				throws KeyManagementException, NoSuchAlgorithmException,
				KeyStoreException, UnrecoverableKeyException {
			super(null);
			sslContext = context;
		}

		@Override
		public Socket createSocket(Socket socket, String host, int port,
				boolean autoClose) throws IOException, UnknownHostException {
			return sslContext.getSocketFactory().createSocket(socket, host,
					port, autoClose);
		}

		@Override
		public Socket createSocket() throws IOException {
			return sslContext.getSocketFactory().createSocket();
		}
	}

	@Override
	public SendRegisterReturnObject sendRegister(String user_id,
			String access_token, String platform, String os_version, String device_name, String androidToken) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("user_id", "" + user_id));
			paramList.add(new BasicNameValuePair("access_token", "" + access_token));
			paramList.add(new BasicNameValuePair("os_version", os_version));
			paramList.add(new BasicNameValuePair("platform", platform));
			paramList.add(new BasicNameValuePair("device_name", device_name));
			paramList.add(new BasicNameValuePair("android_token", androidToken));
			String result = excuteGet(baseUrl + "/sendRegister", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					SendRegisterReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public GetSnapShot getSnapShot(String userId, int startIndex, int limitIndex) {
		// TODO GetSnapShot
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("fbid", userId));
			paramList.add(new BasicNameValuePair("start", String.valueOf(startIndex)));
			paramList.add(new BasicNameValuePair("limit", String.valueOf(limitIndex)));
			
			if(Constants.settingObject!=null && Constants.isChangedSetting){
				paramList.add(new BasicNameValuePair("search_preference[age_from]", String.valueOf(Constants.settingObject.getAge_from())));
				paramList.add(new BasicNameValuePair("search_preference[age_to]", String.valueOf(Constants.settingObject.getAge_to())));
				paramList.add(new BasicNameValuePair("search_preference[filter_female]", Constants.settingObject.getFilter_female()));
				paramList.add(new BasicNameValuePair("search_preference[filter_male]", Constants.settingObject.getFilter_male()));
				paramList.add(new BasicNameValuePair("search_preference[include_friends]", String.valueOf(Constants.settingObject.isInclude_friends())));
				paramList.add(new BasicNameValuePair("search_preference[range]", String.valueOf(Constants.settingObject.getRange())));
				Log.v("Range:", String.valueOf(Constants.settingObject.getRange()) + "");
			}
			String result = excuteGet(baseUrl + "/" + Constants.GET_SNAPSHOT, paramList);
			
			return OakClubJsonParser.getJsonObjectByMapper(result, GetSnapShot.class);
		} catch (Exception e) {
			
		}
		return null;
	}

	@Override
	public HangoutProfileReturnObject getHangoutProfile(String profileId) {
		// TODO HangoutProfile
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", profileId));
			String result = excuteGet(baseUrl+"/"+Constants.GET_HANGOUT_PROFILE,paramList);
			 
			return OakClubJsonParser.getJsonObjectByMapper(result, HangoutProfileReturnObject.class);
		} catch (Exception e) {
			Log.e("OAK", "OAK:Error:"+e.getMessage());
		}
		return null;
	}
	
    @Override
    public ListChatReturnObject getListChat() {
        // TODO Auto-generated method stub
        try{
            String result = excuteGet(baseUrl + "/getListChat", null);
            
            return OakClubJsonParser.getJsonObjectByMapper(result,
                    ListChatReturnObject.class);
        }
        catch(Exception e)
        {
            return null;
        }
    }

	@Override
	public ListPhotoReturnObject getListPhotos(String profile_id) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", "" + profile_id));
			String result = excuteGet(baseUrl + "/getListPhotos", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					ListPhotoReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public GetAccountSettingsReturnObject getAccountSetting() {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			String result = excuteGet(baseUrl + "/getAccountSetting", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					GetAccountSettingsReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

    @Override
    public ChatHistoryReturnObject getHistoryMessages(String profile_id) {
        try {
        	
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("profile_id", "" + profile_id));
            String result = excuteGet(baseUrl + "/getHistoryMessages", paramList);
            return OakClubJsonParser.getJsonObjectByMapper(result,
                    ChatHistoryReturnObject.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public SendMessageReturnObject sendMessage(String to, String msg) {
        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("to", "" + to));
            paramList.add(new BasicNameValuePair("message", "" + msg));
            String result = excutePost(baseUrl + "/chat/post", paramList);
            return OakClubJsonParser.getJsonObjectByMapper(result,
                    SendMessageReturnObject.class);
        } catch (Exception e) {
            return null;
        }
    }

	@Override
	public SetLikeMessageReturnObject SetFavoriteInSnapshot(String profile_id, String numberSet) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", "" + profile_id));
			paramList.add(new BasicNameValuePair("is_like", "" + numberSet)); 
			String result = excutePost(baseUrl + "/"+Constants.SET_FAVORITE, paramList);
			
			return OakClubJsonParser.getJsonObjectByMapper(result,
					SetLikeMessageReturnObject.class);
		} catch (Exception e) {
			String s = e.getMessage();
			Log.e("ERROR", s);
			return null; 
		}
	}

	@Override
	public SettingReturnObject setProfileByField(String field, String value) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair(field, "" + value));
			String result = excutePost(baseUrl + "/setProfileInfo", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					SettingReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public GetSnapshotSettingsReturnObject getSnapshotSetting() {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			String result = excuteGet(baseUrl + "/getSnapshotSetting", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					GetSnapshotSettingsReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SettingReturnObject setSnapshotSetting(String age_from,
			String age_to, String filter_female, String filter_male,
			String fof, String friends, String new_people,
			String purpose_of_search, String range) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("age_from", "" + age_from));
			paramList.add(new BasicNameValuePair("age_to", "" + age_to));
			paramList.add(new BasicNameValuePair("filter_female", "" + filter_female));
			paramList.add(new BasicNameValuePair("filter_male", "" + filter_male));
			paramList.add(new BasicNameValuePair("fof", "" + fof));
			paramList.add(new BasicNameValuePair("friends", "" + friends));
			paramList.add(new BasicNameValuePair("new_people", "" + new_people));
			paramList.add(new BasicNameValuePair("purpose_of_search", "" + purpose_of_search));
			paramList.add(new BasicNameValuePair("range", "" + range));
			
			String result = excutePost(baseUrl + "/setSnapshotSetting", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					SettingReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public UploadPhotoReturnObject UploadProfilePhoto(boolean is_Avatar, File file) {
		try {
            MultipartEntity reqEntity = new MultipartEntity();
            if(is_Avatar)
                reqEntity.addPart("is_avatar", new StringBody("" + is_Avatar));
//          InputStream inputStream = new FileInputStream(file);
//		    byte[] data;
//		    data = getBytes(inputStream);
//		    InputStreamBody inputStreamBody = new InputStreamBody(new ByteArrayInputStream(data), "image.png");
			ContentBody cbFile        = new FileBody( file, "image/png" );
			reqEntity.addPart("photo", cbFile);
			String result = execPostImg(baseUrl + "/uploadPhoto", reqEntity);
			return OakClubJsonParser.getJsonObjectByMapper(result, UploadPhotoReturnObject.class);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@Override
    public UploadVideoObject UploadProfileVideo(File file) {
	    try {
            MultipartEntity reqEntity = new MultipartEntity();
            FileBody fbFile = new FileBody(file);
            reqEntity.addPart("video", fbFile);
            String result = execPostImg(baseUrl + "/uploadVideo", reqEntity);
            return OakClubJsonParser.getJsonObjectByMapper(result, UploadVideoObject.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
	public byte[] getBytes(InputStream inputStream) throws IOException {
	      ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
	      int bufferSize = 1024;
	      byte[] buffer = new byte[bufferSize];

	      int len = 0;
	      while ((len = inputStream.read(buffer)) != -1) {
	        byteBuffer.write(buffer, 0, len);
	      }
	      return byteBuffer.toByteArray();
	    }

	@Override
	public HangoutProfileOtherReturnObject getHangoutProfileOther(String profile_id) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", profile_id));
			String result = excuteGet(baseUrl+"/"+Constants.GET_HANGOUT_PROFILE,paramList);
			 
			return OakClubJsonParser.getJsonObjectByMapper(result, HangoutProfileOtherReturnObject.class);
		} catch (Exception e) {
			Log.e("OAK", "OAK:Error:"+e.getMessage());
		}
		Log.v("null hangoutproflie", "1");
		return null;
	}

	@Override
	public SetLocationReturnObject SetUserLocation(String latitude, String longitude) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("latitude", "" + latitude));
			paramList.add(new BasicNameValuePair("longitude", "" + longitude));
			String result = excutePost(baseUrl + "/setLocation", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					SetLocationReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public SendChatReturnObject SendChatMessage(String toUser, String content) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("to", "" + toUser));
			paramList.add(new BasicNameValuePair("message", "" + content));
			String result = excutePost(baseUrl + "/chat/post", paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					SendChatReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}

    @Override
    public PostMethodReturnObject DeleteUserPhoto(String photo_id) {
        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("photo_id", "" + photo_id));
            String result = excutePost(baseUrl + "/deletePhoto", paramList);
            return OakClubJsonParser.getJsonObjectByMapper(result,
                    PostMethodReturnObject.class);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public PostMethodReturnObject DeleteUserVideo() {
        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            String result = excutePost(baseUrl + "/deleteVideo", paramList);
            return OakClubJsonParser.getJsonObjectByMapper(result,
                    PostMethodReturnObject.class);
        } catch (Exception e) {
            return null;
        }
    }

	@Override
	public GetDataLanguageReturnObject GetDataLanguage() {
        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("country", "" + Constants.country));
            
            String result = excuteGet(baseUrl + "/getDataLanguage", paramList);
            return OakClubJsonParser.getJsonObjectByMapper(result,
            		GetDataLanguageReturnObject.class);
        } catch (Exception e) {
            return null;
        }
	}

    @Override
    public ProfileUpdateFirstTimeObject updateProfileFirstTime(String name, int gender, String birthday,
            String interested, String email, double longitude, double latitude) {
            try {
                List<NameValuePair> paramList = new ArrayList<NameValuePair>();
                paramList.add(new BasicNameValuePair("name", "" + name));
                paramList.add(new BasicNameValuePair("gender", "" + gender));
                paramList.add(new BasicNameValuePair("birthday", "" + birthday));
                paramList.add(new BasicNameValuePair("interested", "" + interested));
                paramList.add(new BasicNameValuePair("email", "" + email));
                paramList.add(new BasicNameValuePair("longitude", "" + longitude));
                paramList.add(new BasicNameValuePair("latitude", "" + latitude));
                String result = excutePost(baseUrl + "/updateProfileFirstTime", paramList);
                return OakClubJsonParser.getJsonObjectByMapper(result,
                        ProfileUpdateFirstTimeObject.class);
            } catch (Exception e) {
                return null;
            }
    }

	@Override
	public void BlockUserById(String profile_id) {
        try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("profile_id", "" + profile_id));
            String result = excutePost(baseUrl + "/blockHangoutProfile", paramList);
        } catch (Exception e) {
        }
	}

	@Override
	public void ReportUserById(String profile_id, String content) {
		try {
            List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("profile_id", "" + profile_id));
            paramList.add(new BasicNameValuePair("content", "" + content));
            String result = excutePost(baseUrl + "/reportUser", paramList);
        } catch (Exception e) {
        }
	}
	
	@Override
	public SetViewMutualReturnObject SetViewedMutualMatch(String profileId) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", "" + profileId));
			String result = excutePost(baseUrl + "/"+Constants.SET_VIEW_MUTUAL_MATCH, paramList);
			return OakClubJsonParser.getJsonObjectByMapper(result,
					SetViewMutualReturnObject.class);
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	public void SetReadMessages(String profile_id) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", profile_id));
			excutePost(baseUrl + "/" + Constants.SET_READ_MESSAGES, paramList);
		} catch (Exception ex) {
			
		}
		
	}
	
	@Override
	public void UpdateLanguage(String key_language) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("appLanguage", key_language));
			excutePost(baseUrl + "/" + Constants.UPDATE_LANGUAGE, paramList);

		} catch (Exception ex){
			
		}
		
	}

	@Override
	public GetConfigData GetConfig() {
		GetConfigData data = null;
		try {
			String result = excuteGet(baseUrl + "/" + Constants.GETCONFIG, null);
			return OakClubJsonParser.getJsonObjectByMapper(result, GetConfigData.class);
		} catch (Exception ex){
			
		}
		
		return data;
	}
	
	@Override
	public VerifiedReturnObject VerifiedUser() {
		try {			
			String result = excuteGet(baseUrl + "/" + Constants.VERIFY_USER, null);
            return OakClubJsonParser.getJsonObjectByMapper(result,
                    VerifiedReturnObject.class);
		} catch (Exception e) {
			return null;
		}	
	}

	@Override
	public VerifiedReturnObject SkipVerified() {
		try {			
			String result = excuteGet(baseUrl + "/" + Constants.SKIP_VERIFIED, null);
            return OakClubJsonParser.getJsonObjectByMapper(result,
                    VerifiedReturnObject.class);
		} catch (Exception e) {
			return null;
		}	
	}
	@Override
	public SenVIPRegisterReturnObject SendVIPRegister(String productID,
			String purchaseToken) {
		try {	
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("product_id", productID));
			paramList.add(new BasicNameValuePair("receipt", purchaseToken));
			String result = excutePost(baseUrl + "/" + Constants.VIP_REGISTER, paramList);
			Log.d("result receipt", result);
			return OakClubJsonParser.getJsonObjectByMapper(result,
                    SenVIPRegisterReturnObject.class);
		} catch (Exception e) {
			Log.d("result receipt", "error request to server.....");
			return null;
		}
	}

	 
    //For Service japi
    @Override
    public HashMap<String, Object> getChatList() {
        try{
            String result = excuteGet(baseUrl + "/getChatList", null);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map = OakClubJsonParser.getHashmapByMapper(result);
            return map;
        }
        catch(Exception e)
        {
            return null;
        }
    }
    
    //For Service japi
    @Override
    public HashMap<String, Object> getChatHistory(String profileId) {
        try{
        	List<NameValuePair> paramList = new ArrayList<NameValuePair>();
            paramList.add(new BasicNameValuePair("profile_id", "" + profileId));
            String result = excuteGet(baseUrl + "/getChatHistory", paramList);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map = OakClubJsonParser.getHashmapByMapper(result);
            return map;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    //For Service japi
	@Override
	public HashMap<String, Object> sendChatMessage(String profileId, String message) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", "" + profileId));
			paramList.add(new BasicNameValuePair("message", "" + message));
			String result = excutePost(baseUrl + "/sendChatMessage", paramList);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map = OakClubJsonParser.getHashmapByMapper(result);
            return map;
		} catch (Exception e) {
			return null;
		}
	}

    //For Service japi
	@Override
	public HashMap<String, Object> readChatMessage(String profileId) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", profileId));

			String result = excutePost(baseUrl + "/readChatMessage", paramList);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map = OakClubJsonParser.getHashmapByMapper(result);
            return map;
		} catch (Exception ex) {
			return null;
		}
		
	}
	
    //For Service japi
	@Override
	public HashMap<String, Object> addRoster(String profileId) {
		try {
			List<NameValuePair> paramList = new ArrayList<NameValuePair>();
			paramList.add(new BasicNameValuePair("profile_id", profileId));

			String result = excutePost(baseUrl + "/addRoster", paramList);
            HashMap<String, Object> map = new HashMap<String, Object>();
            map = OakClubJsonParser.getHashmapByMapper(result);
            return map;
		} catch (Exception ex) {
			return null;
		}
		
	}
}
