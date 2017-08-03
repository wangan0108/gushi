package com.maven.web.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.io.IOUtils;

public class GZipUtils {
	public static byte[] compress(byte[] byteUncompress) throws IOException {
		ByteArrayOutputStream out = null;
		GZIPOutputStream gout = null;
		try {
			out = new ByteArrayOutputStream();
			gout = new GZIPOutputStream(out);
			gout.write(byteUncompress);
		} finally {
			IOUtils.closeQuietly(gout);
			IOUtils.closeQuietly(out);
		}

		return out.toByteArray();
	}

	public static byte[] unCompress(InputStream inputStream) throws IOException {
		GZIPInputStream gzipIn = null;
		try {
			gzipIn = new GZIPInputStream(inputStream);
			byte[] bt = IOUtils.toByteArray(gzipIn);
			return bt;
		} finally {
			IOUtils.closeQuietly(gzipIn);
		}

	}

	public static void main(String[] args) throws Exception {
		String strUncompress = "sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地sd左动手术食道癌枯在在地";
		byte[] byteBefore = strUncompress.getBytes("UTF-8");
		System.out.println("byteBefore>>>" + byteBefore.length);

		byte[] byteAfter = compress(byteBefore);
		System.out.println("byteAfter>>>" + byteAfter.length);

		byte[] result = unCompress(new ByteArrayInputStream(byteAfter));
		System.out.println(new String(result, "UTF-8"));
		// System.out.println("uncompress>>>" + bb.length);
	}
}
