package com.mcts.app.volley;

public interface IVolleyRespose{

		/**
		 * @param mRes means the response of webService
		 * @param ResponseTag means It display response of which Web Method .
		 * @param responseCode send response code if 200 then response ok , if 404 then Error. 
		 */
      public void onVolleyResponse(int responseCode, String mRes, String ResponseTag);
      
      /**
		 * @param Code means the Error code 404  "there is something going wrong "
		 * @param ResponseTag means It display Error of which Web Method .
		 * @param mError send response "Error" when Error. 
		 */
      public void onVolleyError(int Code, String mError, String ResponseTag);
      
      
}
