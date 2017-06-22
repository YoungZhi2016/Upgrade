package com.player.upgrade.download;

import java.util.concurrent.CompletableFuture;

import com.player.upgrade.index.Upgrade;
import com.player.upgrade.utils.Logs;
import com.winone.ftc.mcore.imps.ManagerImp;
import com.winone.ftc.mentity.mbean.State;
import com.winone.ftc.mentity.mbean.Task;
import com.winone.ftc.mentity.mbean.Task.onResult;
import com.winone.ftc.mentity.mbean.TaskFactory;

public final class DownloadServer {

	private static class BuilderHolper {
		private static DownloadServer downloadServer = new DownloadServer();
	}

	private DownloadServer() {
	}

	public static DownloadServer getInstance() {
		return BuilderHolper.downloadServer;
	}

	public void down(String url, String fileName) {
		switch (getDownloadWayByUrl(url)) {
		case DownloadStrategy.FTP_WAY:
			ftpDownloadTask(url, DownloadStrategy.UPGRADE_DIR, fileName, true);
			break;
		case DownloadStrategy.HTTP_WAY:
			httpDownloadTask(url, "GET", DownloadStrategy.UPGRADE_DIR, fileName, true);
			break;
		}
	}

	private String getDownloadWayByUrl(String url) {
		String uString = url.toUpperCase();
		if (uString.startsWith(DownloadStrategy.FTP_WAY))
			return DownloadStrategy.FTP_WAY;
		else if (uString.startsWith(DownloadStrategy.HTTP_WAY)) {
			return DownloadStrategy.HTTP_WAY;
		}
		return DownloadStrategy.UNKNOWN_WAY;
	}

	/**
	 * HTTP方式下载
	 * 
	 * @param url
	 * 
	 * @param httpType
	 *            GET or PUT
	 * 
	 * @param localDir
	 *            本地目录
	 * @param fileName
	 *            文件名
	 * @param isCover
	 *            是否覆盖
	 */
	@SuppressWarnings("serial")
	private void httpDownloadTask(String url, String httpType, String localDir, String fileName, boolean isCover) {
		CompletableFuture.supplyAsync(() -> {
			Task task = TaskFactory.httpTaskDown(url, httpType, localDir, fileName, isCover);
			task.setOnResult(new onResult() {
				@Override
				public void onSuccess(State state) {
					notice(localDir + "/" + fileName);
				}

				@Override
				public void onLoading(State state) {
				}

				@Override
				public void onFailt(State state) {
					Logs.error(url + "--download error--" + state.toString());
				}
			});
			ManagerImp.get().load(task);
			return 0;
		});
	}

	/**
	 * FTP方式下载
	 * 
	 * @param url
	 *            下载地址
	 * @param localDir
	 *            本地目录
	 * @param fileName
	 *            文件名
	 * @param isCover
	 *            是否覆盖
	 */
	@SuppressWarnings("serial")
	private void ftpDownloadTask(String url, String localDir, String fileName, boolean isCover) {
		CompletableFuture.supplyAsync(() -> {
			Task task = TaskFactory.ftpTaskDown(url, localDir, fileName, true);
			task.setOnResult(new onResult() {
				@Override
				public void onSuccess(State state) {
					notice(localDir + "/" + fileName);
				}

				@Override
				public void onLoading(State state) {

				}

				@Override
				public void onFailt(State state) {
					Logs.error(url + "--download error--" + state.toString());
				}
			});
			ManagerImp.get().load(task);
			return 0;
		});
	}

	/**
	 * get current task size
	 */
	private int getCurrenTaskSize() {
		return ManagerImp.get().getCurrenTaskQueue().size();
	}

	/**
	 * notice
	 * 
	 * @param filePath
	 */
	private void notice(String filePath) {
		int size = getCurrenTaskSize();
		System.err.println("size: " + size);
		if (size == 0) {
			Logs.info("tell: " + filePath);
			Upgrade.unZipPack(filePath);
		}
	}

}