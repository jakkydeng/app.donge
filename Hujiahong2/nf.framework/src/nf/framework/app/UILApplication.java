/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package nf.framework.app;


import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import nf.framework.core.AbsApplication;
import nf.framework.core.exception.CrashHandler.CrashMessageCallBack;
import nf.framework.core.exception.LogUtil;


/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class UILApplication extends AbsApplication {
	@Override
	public void onCreate() {
		super.onCreate();
		initImageLoader(getApplicationContext());
		LogUtil.OpenBug=true;
	}

	public  void initImageLoader(Context context) {
		
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
		config.threadPoolSize(1);
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();
		config.memoryCache(new WeakMemoryCache());
		config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs(); // Remove for release app

		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config.build());
		
	}
	@Override
	public CrashMessageCallBack getCrashMessageCallBack() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}