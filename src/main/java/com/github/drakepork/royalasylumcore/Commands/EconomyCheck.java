package com.github.drakepork.royalasylumcore.Commands;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Containers.CMIUser;
import com.github.drakepork.royalasylumcore.Core;
import com.google.inject.Inject;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

public class EconomyCheck implements CommandExecutor {
	private Core plugin;

	@Inject
	public EconomyCheck(Core plugin) {
		this.plugin = plugin;
	}


	LinkedHashMap<String, Integer> shopLogAmount = new LinkedHashMap<>();
	LinkedHashMap<String, Double> shopLogPrice = new LinkedHashMap<>();
	LinkedHashMap<String, Integer> shopLogPage = new LinkedHashMap<>();

	public void openGUI(Player player, int page, String sortMethod) {
		ArrayList<Double> shopLogPriceList = new ArrayList<>();
		ArrayList<Integer> shopLogAmountList = new ArrayList<>();
		if(sortMethod.equalsIgnoreCase("amounttop")) {
			int pageNew = 0;
			int i = 0;
			shopLogPage = new LinkedHashMap<>();
			LinkedHashMap<String, Integer> shopLogAmountSorted = new LinkedHashMap<>();
			for (HashMap.Entry<String, Integer> entry : shopLogAmount.entrySet()) {
				shopLogAmountList.add(entry.getValue());
			}
			Collections.sort(shopLogAmountList, Collections.reverseOrder());
			for (int num : shopLogAmountList) {
				for (HashMap.Entry<String, Integer> entry : shopLogAmount.entrySet()) {
					if (entry.getValue().equals(num)) {
						shopLogAmountSorted.put(entry.getKey(), num);
					}
				}
			}
			for (HashMap.Entry<String, Integer> entry : shopLogAmountSorted.entrySet()) {
				if(i == 45) {
					pageNew = 1 + pageNew;
					i = 0;
				}
				shopLogPage.put(entry.getKey(), pageNew);
				i++;
			}
		} else if(sortMethod.equalsIgnoreCase("amountbottom")) {
			int pageNew = 0;
			int i = 0;
			shopLogPage = new LinkedHashMap<>();
			LinkedHashMap<String, Integer> shopLogAmountSorted = new LinkedHashMap<>();
			for (HashMap.Entry<String, Integer> entry : shopLogAmount.entrySet()) {
				shopLogAmountList.add(entry.getValue());
			}
			Collections.sort(shopLogAmountList);
			for (int num : shopLogAmountList) {
				for (HashMap.Entry<String, Integer> entry : shopLogAmount.entrySet()) {
					if (entry.getValue().equals(num)) {
						shopLogAmountSorted.put(entry.getKey(), num);
					}
				}
			}
			for (HashMap.Entry<String, Integer> entry : shopLogAmountSorted.entrySet()) {
				if(i == 45) {
					pageNew = 1 + pageNew;
					i = 0;
				}
				shopLogPage.put(entry.getKey(), pageNew);
				i++;
			}
		} else if(sortMethod.equalsIgnoreCase("moneytop")) {
			int pageNew = 0;
			int i = 0;
			shopLogPage = new LinkedHashMap<>();
			LinkedHashMap<String, Double> shopLogPriceSorted = new LinkedHashMap<>();
			for (HashMap.Entry<String, Double> entry : shopLogPrice.entrySet()) {
				shopLogPriceList.add(entry.getValue());
			}
			Collections.sort(shopLogPriceList, Collections.reverseOrder());
			for (double num : shopLogPriceList) {
				for (HashMap.Entry<String, Double> entry : shopLogPrice.entrySet()) {
					if (entry.getValue().equals(num)) {
						shopLogPriceSorted.put(entry.getKey(), num);
					}
				}
			}
			for (HashMap.Entry<String, Double> entry : shopLogPriceSorted.entrySet()) {
				if(i == 45) {
					pageNew = 1 + pageNew;
					i = 0;
				}
				shopLogPage.put(entry.getKey(), pageNew);
				i++;
			}
		} else if(sortMethod.equalsIgnoreCase("moneybottom")) {
			int pageNew = 0;
			int i = 0;
			shopLogPage = new LinkedHashMap<>();
			LinkedHashMap<String, Double> shopLogPriceSorted = new LinkedHashMap<>();
			for (HashMap.Entry<String, Double> entry : shopLogPrice.entrySet()) {
				shopLogPriceList.add(entry.getValue());
			}
			Collections.sort(shopLogPriceList);
			for (double num : shopLogPriceList) {
				for (HashMap.Entry<String, Double> entry : shopLogPrice.entrySet()) {
					if (entry.getValue().equals(num)) {
						shopLogPriceSorted.put(entry.getKey(), num);
					}
				}
			}
			for (HashMap.Entry<String, Double> entry : shopLogPriceSorted.entrySet()) {
				if(i == 45) {
					pageNew = 1 + pageNew;
					i = 0;
				}
				shopLogPage.put(entry.getKey(), pageNew);
				i++;
			}
		}


		ArrayList totalPages = new ArrayList<>();
		Inventory shopLogInv = Bukkit.createInventory(null, 54, ChatColor.RED + "Shop Log | Page " + page);
		int i = 0;
		for (HashMap.Entry<String, Integer> entry : shopLogPage.entrySet()) {
			if(entry.getValue() == page) {
				ArrayList lore = new ArrayList();
				String mat = entry.getKey().replaceAll(" ", "_").toUpperCase();
				ItemStack item = new ItemStack(Material.valueOf(mat));
				ItemMeta meta = item.getItemMeta();
				meta.setDisplayName(ChatColor.YELLOW + entry.getKey());
				DecimalFormat df = new DecimalFormat("###,###,###");
				lore.add(ChatColor.GRAY + "Amount Sold: " + ChatColor.YELLOW + df.format(shopLogAmount.get(entry.getKey())));
				NumberFormat defaultFormat = NumberFormat.getCurrencyInstance();
				lore.add(ChatColor.GRAY + "Money Made: " + ChatColor.YELLOW + defaultFormat.format(shopLogPrice.get(entry.getKey())));
				meta.setLore(lore);
				item.setItemMeta(meta);
				shopLogInv.setItem(i, item);
				i++;
			}
			totalPages.add(entry.getValue());
		}

		ItemStack pane = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemStack pageChange = new ItemStack(Material.PAPER);
		ItemStack itemSort = new ItemStack(Material.BOOK);
		ItemMeta metaSort = itemSort.getItemMeta();
		ItemMeta itemMeta = pageChange.getItemMeta();
		itemMeta.setDisplayName(ChatColor.GREEN + "Next Page");
		pageChange.setItemMeta(itemMeta);
		for (int b = 45; b < 54; b++) {
			if (page == 0) {
				if(totalPages.size() < 1) {
					if (b == 47) {
						metaSort.setDisplayName(ChatColor.GREEN + "Top Sold");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 48) {
						metaSort.setDisplayName(ChatColor.GREEN + "Least Sold");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 49) {
						metaSort.setDisplayName(ChatColor.GREEN + "Player Search");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 50) {
						metaSort.setDisplayName(ChatColor.GREEN + "least Money Made");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 51) {
						metaSort.setDisplayName(ChatColor.GREEN + "Top Money Made");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else {
						shopLogInv.setItem(b, pane);
					}
				} else {
					if (Collections.max(totalPages).equals(page)) {
						if (b == 47) {
							metaSort.setDisplayName(ChatColor.GREEN + "Top Sold");
							itemSort.setItemMeta(metaSort);
							shopLogInv.setItem(b, itemSort);
						} else if (b == 48) {
							metaSort.setDisplayName(ChatColor.GREEN + "Least Sold");
							itemSort.setItemMeta(metaSort);
							shopLogInv.setItem(b, itemSort);
						} else if (b == 49) {
							metaSort.setDisplayName(ChatColor.GREEN + "Player Search");
							itemSort.setItemMeta(metaSort);
							shopLogInv.setItem(b, itemSort);
						} else if (b == 50) {
							metaSort.setDisplayName(ChatColor.GREEN + "least Money Made");
							itemSort.setItemMeta(metaSort);
							shopLogInv.setItem(b, itemSort);
						} else if (b == 51) {
							metaSort.setDisplayName(ChatColor.GREEN + "Top Money Made");
							itemSort.setItemMeta(metaSort);
							shopLogInv.setItem(b, itemSort);
						} else {
							shopLogInv.setItem(b, pane);
						}
					} else {
						if(b != 52) {
							if (b == 47) {
								metaSort.setDisplayName(ChatColor.GREEN + "Top Sold");
								itemSort.setItemMeta(metaSort);
								shopLogInv.setItem(b, itemSort);
							} else if (b == 48) {
								metaSort.setDisplayName(ChatColor.GREEN + "Least Sold");
								itemSort.setItemMeta(metaSort);
								shopLogInv.setItem(b, itemSort);
							} else if (b == 49) {
								metaSort.setDisplayName(ChatColor.GREEN + "Player Search");
								itemSort.setItemMeta(metaSort);
								shopLogInv.setItem(b, itemSort);
							} else if (b == 50) {
								metaSort.setDisplayName(ChatColor.GREEN + "least Money Made");
								itemSort.setItemMeta(metaSort);
								shopLogInv.setItem(b, itemSort);
							} else if (b == 51) {
								metaSort.setDisplayName(ChatColor.GREEN + "Top Money Made");
								itemSort.setItemMeta(metaSort);
								shopLogInv.setItem(b, itemSort);
							} else {
								shopLogInv.setItem(b, pane);
							}
						} else {
							shopLogInv.setItem(b, pageChange);
						}
					}
				}
			} else if (Collections.max(totalPages).equals(page)) {
				if(b != 46) {
					if (b == 47) {
						metaSort.setDisplayName(ChatColor.GREEN + "Top Sold");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 48) {
						metaSort.setDisplayName(ChatColor.GREEN + "Least Sold");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 49) {
						metaSort.setDisplayName(ChatColor.GREEN + "Player Search");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 50) {
						metaSort.setDisplayName(ChatColor.GREEN + "least Money Made");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 51) {
						metaSort.setDisplayName(ChatColor.GREEN + "Top Money Made");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else {
						shopLogInv.setItem(b, pane);
					}
				} else {
					itemMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
					pageChange.setItemMeta(itemMeta);
					shopLogInv.setItem(b, pageChange);
				}
			} else {
				if(b != 46 && b != 52) {
					if (b == 47) {
						metaSort.setDisplayName(ChatColor.GREEN + "Top Sold");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 48) {
						metaSort.setDisplayName(ChatColor.GREEN + "Least Sold");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 49) {
						metaSort.setDisplayName(ChatColor.GREEN + "Player Search");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 50) {
						metaSort.setDisplayName(ChatColor.GREEN + "least Money Made");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else if (b == 51) {
						metaSort.setDisplayName(ChatColor.GREEN + "Top Money Made");
						itemSort.setItemMeta(metaSort);
						shopLogInv.setItem(b, itemSort);
					} else {
						shopLogInv.setItem(b, pane);
					}
				} else if(b == 46) {
					itemMeta.setDisplayName(ChatColor.GREEN + "Previous Page");
					pageChange.setItemMeta(itemMeta);
					shopLogInv.setItem(b, pageChange);
				} else {
					shopLogInv.setItem(b, pageChange);
				}
			}
		}
		player.openInventory(shopLogInv);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		shopLogAmount = new LinkedHashMap<>();
		shopLogPrice = new LinkedHashMap<>();
		shopLogPage = new LinkedHashMap<>();

		ArrayList<Double> shopLogPriceList = new ArrayList<>();
		ArrayList<Integer> shopLogAmountList = new ArrayList<>();
		if(sender instanceof Player) {
			Player player = (Player) sender;
			String sortMethod = "default";
			if(args.length > 0 && args.length <= 1) {
				sortMethod = args[0];
			}
			if(args.length > 0 && args.length <= 2) {
				if (args[0].equalsIgnoreCase("player")) {
					if(CMI.getInstance().getPlayerManager().getUser(args[1]) != null) {
						try {
							FileInputStream fstream = new FileInputStream(Bukkit.getPluginManager()
									.getPlugin("ShopGUIPlus").getDataFolder()
									+ File.separator + "shop.log");
							BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
							String strLine;
							int page = 0;
							int i = 0;
							while ((strLine = br.readLine()) != null) {
								if (strLine.contains(";")) {
									String[] str = strLine.split(";");
									if (str.length > 0) {
										if (str[2].equalsIgnoreCase("sold")) {
											if(str[1].equalsIgnoreCase(args[1])) {
												int shopAmount = Integer.parseInt(str[3].replaceAll(",", ""));
												String shopPriceString = str[5].replaceAll(",", "");
												double shopPrice = Double.parseDouble(shopPriceString.replaceAll("\\$", ""));
												if (shopLogAmount.containsKey(str[4])) {
													int newNum = shopAmount + shopLogAmount.get(str[4]);
													shopLogAmount.put(str[4], newNum);
												} else {
													shopLogAmount.put(str[4], shopAmount);
													if (i == 45) {
														page = 1 + page;
														i = 0;
													}
													shopLogPage.put(str[4], page);
													i++;
												}

												if (shopLogPrice.containsKey(str[4])) {
													double newNum = shopPrice + shopLogPrice.get(str[4]);
													shopLogPrice.put(str[4], newNum);
												} else {
													shopLogPrice.put(str[4], shopPrice);
												}
											}
										}
									} else {
										continue;
									}
								}
							}
							fstream.close();
						} catch (Exception e) {
							System.err.println("Error: " + e.getMessage());
						}
					}
				}
			} else {
				try {
					FileInputStream fstream = new FileInputStream(Bukkit.getPluginManager()
							.getPlugin("ShopGUIPlus").getDataFolder()
							+ File.separator + "shop.log");
					BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
					String strLine;
					int page = 0;
					int i = 0;
					while ((strLine = br.readLine()) != null) {
						if (strLine.contains(";")) {
							String[] str = strLine.split(";");
							if (str.length > 0) {
								if (str[2].equalsIgnoreCase("sold")) {
									int shopAmount = Integer.parseInt(str[3].replaceAll(",", ""));
									String shopPriceString = str[5].replaceAll(",", "");
									double shopPrice = Double.parseDouble(shopPriceString.replaceAll("\\$", ""));
									if (shopLogAmount.containsKey(str[4])) {
										int newNum = shopAmount + shopLogAmount.get(str[4]);
										shopLogAmount.put(str[4], newNum);
									} else {
										shopLogAmount.put(str[4], shopAmount);
										if (i == 45) {
											page = 1 + page;
											i = 0;
										}
										shopLogPage.put(str[4], page);
										i++;
									}

									if (shopLogPrice.containsKey(str[4])) {
										double newNum = shopPrice + shopLogPrice.get(str[4]);
										shopLogPrice.put(str[4], newNum);
									} else {
										shopLogPrice.put(str[4], shopPrice);
									}
								}
							} else {
								continue;
							}
						}
					}
					fstream.close();
				} catch (Exception e) {
					System.err.println("Error: " + e.getMessage());
				}
			}

				openGUI(player, 0, sortMethod);
		}
		return true;
	}

}
