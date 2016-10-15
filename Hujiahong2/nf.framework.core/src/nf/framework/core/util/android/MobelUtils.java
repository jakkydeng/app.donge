package nf.framework.core.util.android;

import android.content.Context;
import android.view.WindowManager;

public class MobelUtils {

	public static int getCurrentPhoneWidth(Context context) {
		WindowManager wm = (WindowManager)context.getSystemService(
				Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}

	/**
	 * 获取手机宽or高数组
	 * 
	 * @return
	 */
	public static int[] getMobelPicWorh() {
		int wh[] = { 1080, 720 };
		String mobel = android.os.Build.MODEL;

		// 移动版note3
		if (mobel.equals("SM-N9006")) {
			// wh[0] = 3264;
			// wh[1] = 2448;
			wh[0] = 2560;
			wh[1] = 1920;
		} else {
			// 移动版note2
			if (mobel.equals("GT-N7100")) {
				wh[0] = 1600;
				wh[1] = 1200;
			} else {
				// 移动版s4
				if (mobel.equals("SCH-I959")) {
					// wh[0] = 3264;
					// wh[1] = 2448;
					wh[0] = 2560;
					wh[1] = 1920;
				} else {
					// 电信版note2
					if (mobel.equals("SCH-N719")) {
						wh[0] = 2560;
						wh[1] = 1920;
					} else {
						// 三星tp3
						if (mobel.equals("SM-T210")) {
							wh[0] = 1536;
							wh[1] = 864;
						} else {
							// 小米2s
							if (mobel.equals("MI 2S")) {
								wh[0] = 2592;
								wh[1] = 1936;
							} else {
								// 小米3
								if (mobel.equals("MI 3")) {
									// beng = 3264;
									wh[0] = 3264;
									wh[1] = 2448;
								} else {
									if (mobel.equals("HUAWEI G525-U00")) {
										wh[0] = 2048;
										wh[1] = 1536;
									} else {
										if (mobel.equals("HUAWEI B199")) {
											wh[0] = 3200;
											wh[1] = 2400;
										} else {
											if (mobel.equals("HUAWEI A199")) {
												wh[0] = 1920;
												wh[1] = 1088;
											} else {
												if (mobel.equals("X9007")) {
													wh[0] = 2592;
													wh[1] = 1944;
												} else {
													if (mobel.equals("ZTE U5")) {
														wh[0] = 2592;
														wh[1] = 1944;
													} else {
														if (mobel
																.equals("ZTE-T U960s")) {
															wh[0] = 1600;
															wh[1] = 1200;
														} else {
															if (mobel
																	.equals("Lenovo K910")) {
																wh[0] = 3200;
																wh[1] = 2400;
															} else {
																if (mobel
																		.equals("HTC D816w")) {
																	wh[0] = 3264;
																	wh[1] = 2448;
																} else {
																	if (mobel
																			.equals("HUAWEI MediaPad")) {
																		wh[0] = 1600;
																		wh[1] = 2448;
																	} else {
																		if (mobel
																				.equals("LT26i")) {
																			wh[0] = 1632;
																			wh[1] = 1224;
																		} else {
																			if (mobel
																					.equals("M353")) {
																				wh[0] = 1920;
																				wh[1] = 1080;
																			}

																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return wh;

	}

	/**
	 * 获取手机宽or高数组 显示用
	 * 
	 * @return
	 */
	public static int[] getShowMobelPicWorh() {
		int wh[] = { 1600, 1200 };
		String mobel = android.os.Build.MODEL;

		// 移动版note3
		if (mobel.equals("SM-N9006")) {
			wh[0] = 2560;
			wh[1] = 1920;
		} else {
			// 移动版note2
			if (mobel.equals("GT-N7100")) {
				wh[0] = 1600;
				wh[1] = 1200;
			} else {
				// 移动版s4
				if (mobel.equals("SCH-I959")) {
					wh[0] = 2560;
					wh[1] = 1920;
				} else {
					// 电信版note2
					if (mobel.equals("SCH-N719")) {
						wh[0] = 2560;
						wh[1] = 1920;
					} else {
						// 三星tp3
						if (mobel.equals("SM-T210")) {
							wh[0] = 1536;
							wh[1] = 864;
						} else {
							// 小米2s
							if (mobel.equals("MI 2S")) {
								wh[0] = 2592;
								wh[1] = 1936;
							} else {
								// 小米3
								if (mobel.equals("MI 3")) {
									// beng = 3264;
									wh[0] = 2560;
									wh[1] = 1920;
								} else {
									if (mobel.equals("HUAWEI G525-U00")) {
										wh[0] = 2048;
										wh[1] = 1536;
									} else {
										if (mobel.equals("HUAWEI B199")) {
											wh[0] = 2560;
											wh[1] = 1920;
										} else {
											if (mobel.equals("HUAWEI A199")) {
												wh[0] = 1920;
												wh[1] = 1088;
											} else {
												if (mobel.equals("X9007")) {
													wh[0] = 2592;
													wh[1] = 1944;
												} else {
													if (mobel.equals("ZTE U5")) {
														wh[0] = 2592;
														wh[1] = 1944;
													} else {
														if (mobel
																.equals("ZTE-T U960s")) {
															wh[0] = 1600;
															wh[1] = 1200;
														} else {
															if (mobel
																	.equals("Lenovo K910")) {
																wh[0] = 2560;
																wh[1] = 1920;
															} else {
																if (mobel
																		.equals("HTC D816w")) {
																	wh[0] = 2560;
																	wh[1] = 1920;
																} else {
																	if (mobel
																			.equals("HUAWEI MediaPad")) {
																		wh[0] = 1600;
																		wh[1] = 2448;
																	} else {
																		if (mobel
																				.equals("LT26i")) {
																			wh[0] = 1632;
																			wh[1] = 1224;
																		} else {
																			if (mobel
																					.equals("M353")) {
																				wh[0] = 1920;
																				wh[1] = 1080;
																			}

																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return wh;

	}

	/**
	 * 获取手机宽
	 * 
	 * @return
	 */
	public static int getMobelPicW() {
		String mobel = android.os.Build.MODEL;
		int beng = 1600;
		// 移动版note3
		if (mobel.equals("SM-N9006")) {
			// beng = 3264;
			beng = 2560;
		} else {
			// 移动版note2
			if (mobel.equals("GT-N7100")) {
				beng = 2560;
			} else {
				// 移动版s4
				if (mobel.equals("SCH-I959")) {
					// beng = 3264;
					beng = 2560;
				} else {
					// 电信版note2
					if (mobel.equals("SCH-N719")) {
						beng = 2560;
					} else {
						// 三星tp3
						if (mobel.equals("SM-T210")) {
							beng = 1536;
						} else {
							// 小米2s
							if (mobel.equals("MI 2S")) {
								beng = 2592;
							} else {
								// 小米3
								if (mobel.equals("MI 3")) {
									beng = 3264;
								} else {
									if (mobel.equals("HUAWEI G525-U00")) {
										beng = 2048;
									} else {
										if (mobel.equals("HUAWEI B199")) {
											beng = 3200;
										} else {
											if (mobel.equals("HUAWEI A199")) {
												beng = 1920;
											} else {
												if (mobel.equals("X9007")) {
													beng = 2592;
												} else {
													if (mobel.equals("ZTE U5")) {
														beng = 2592;
													} else {
														if (mobel
																.equals("ZTE-T U960s")) {
															beng = 1600;
														} else {
															if (mobel
																	.equals("Lenovo K910")) {
																beng = 2048;
															} else {
																if (mobel
																		.equals("HTC D816w")) {
																	beng = 3264;
																} else {
																	if (mobel
																			.equals("HUAWEI MediaPad")) {
																		beng = 1600;
																	} else {
																		if (mobel
																				.equals("LT26i")) {
																			beng = 1632;
																		} else {
																			if (mobel
																					.equals("M353")) {
																				beng = 1920;
																			}

																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		// System.out.println(mobel+"======================="+beng);
		return beng;
	}

}
