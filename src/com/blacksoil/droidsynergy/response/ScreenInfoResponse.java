package com.blacksoil.droidsynergy.response;

import com.blacksoil.droidsynergy.global.DroidSynergyShared;

/* Response corresponds to ScreenInfoPacket
 * The reply:
 * DINF%2i%2i%2i%2i%2i%2i%2i
 * %2i(s) respectively are:
 * x -> mouse x coord
 * y -> mouse y coord
 * clientWidth  -> screen width 
 * clientHeight -> screen height
 * warp -> 0?
 * mx -> 0?
 * my -> 0?
 */
public class ScreenInfoResponse extends BasicResponse {
	private int mScreenX, mScreenY, mClientWidth, mClientHeight, mWarp, mMx,
			mMy;

	public ScreenInfoResponse(){
		mScreenX = 0;
		mScreenY = 0;
		mClientHeight = DroidSynergyShared.getInstance().getScreenHeight();
		mClientWidth = DroidSynergyShared.getInstance().getScreenWidth();
		mWarp = 0;
		mMx = 0;
		mMy = 0;
		
		appendString("DINF");
		appendInt16(mScreenX);
		appendInt16(mScreenY);
		appendInt16(mClientWidth);
		appendInt16(mClientHeight);
		appendInt16(mWarp);
		appendInt16(mMx);
		appendInt16(mMy);
	}
}
