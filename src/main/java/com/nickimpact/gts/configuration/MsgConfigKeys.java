/*
 * This file is part of LuckPerms, licensed under the MIT License.
 *
 *  Copyright (c) lucko (Luck) <luck@lucko.me>
 *  Copyright (c) contributors
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in all
 *  copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *  SOFTWARE.
 */

package com.nickimpact.gts.configuration;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.nickimpact.impactor.api.configuration.ConfigKey;
import com.nickimpact.impactor.api.configuration.IConfigKeys;
import com.nickimpact.impactor.api.configuration.keys.ListKey;
import com.nickimpact.impactor.api.configuration.keys.StringKey;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MsgConfigKeys implements IConfigKeys {

	// Plugin chat prefix (replacement option for {{gts_prefix}}
	public static final ConfigKey<String> PREFIX = StringKey.of("general.gts-prefix", "&eGTS &7\u00bb");
	public static final ConfigKey<String> ERROR_PREFIX = StringKey.of("general.gts-prefix-error", "&eGTS &7(&cERROR&7)");

	// Generic messages for the program
	// Best to support lists of text here, as a server may decide to go heavy on text formatting
	public static final ConfigKey<List<String>> MAX_LISTINGS = ListKey.of("general.max-listings", Lists.newArrayList(
			"{{gts_prefix}} &cUnfortunately, you can't deposit another listing, since you already have {{max_listings}} deposited..."
	));
	public static final ConfigKey<List<String>> ADD_TEMPLATE = ListKey.of("general.addition-to-seller", Lists.newArrayList(
			"{{gts_prefix}} &7Your &a{{listing_name}} &7has been added to the market!"
	));
	public static final ConfigKey<List<String>> TAX_APPLICATION = ListKey.of("general.taxes.applied", Lists.newArrayList(
			"&c&l- {{tax}} &7(&aTaxes&7)"
	));
	public static final ConfigKey<List<String>> TAX_INVALID = ListKey.of("general.taxes.invalid", Lists.newArrayList(
			"{{gts_prefix}} &cUnable to afford the tax of &e{{tax}} &cfor this listing..."
	));
	public static final ConfigKey<List<String>> ADD_BROADCAST = ListKey.of("general.addition-broadcast", Lists.newArrayList(
			"{{gts_prefix}} &c{{player}} &7has added a &a{{listing_specifics}} &7to the GTS for &a{{price}}&7!"
	));
	public static final ConfigKey<List<String>> PURCHASE_PAY = ListKey.of("general.prices.pay", Lists.newArrayList(
			"{{gts_prefix}} &7You have purchased a &a{{listing_specifics}} &7for &e{{price}}&7!"
	));
	public static final ConfigKey<List<String>> PURCHASE_RECEIVE = ListKey.of("general.prices.receive", Lists.newArrayList(
			"{{gts_prefix}} &7You have received your price of &e{{price}} from your &a{{listing_name}} &7listing!"
	));
	public static final ConfigKey<List<String>> AUCTION_BID_BROADCAST = ListKey.of("general.auctions.bid", Lists.newArrayList(
			"{{gts_prefix}} &e{{player}} &7has placed a bid on the &a{{listing_specifics}}!"
	));
	public static final ConfigKey<List<String>> AUCTION_BID = ListKey.of("general.auctions.bid-personal", Lists.newArrayList(
			"{{gts_prefix}} &7Your bid has been placed! If you win, you will pay &e{{price}}&7!"
	));
	public static final ConfigKey<List<String>> AUCTION_WIN_BROADCAST = ListKey.of("general.auctions.win", Lists.newArrayList(
			"{{gts_prefix}} &e{{player}} &7has won the auction for the &a{{listing_specifics}}!"
	));
	public static final ConfigKey<List<String>> AUCTION_WIN = ListKey.of("general.auctions.win-personal", Lists.newArrayList(
			"{{gts_prefix}} &7Congrats! You've won the auction on the &e{{listing_specifics}} &7for &a{{price}}&7!"
	));
	public static final ConfigKey<List<String>> AUCTION_SOLD = ListKey.of("general.auctions.sold", Lists.newArrayList(
			"{{gts_prefix}} &7Your &e{{listing_specifics}} &7auction was sold to &e{{high_bidder}} &7for &a{{price}}&7!"
	));
	public static final ConfigKey<List<String>> AUCTION_IS_HIGH_BIDDER = ListKey.of("general.auctions.is-high-bidder", Lists.newArrayList(
			"{{gts_prefix}} &cHold off! You wouldn't want to bid against yourself!"
	));
	public static final ConfigKey<List<String>> REMOVAL_CHOICE = ListKey.of("general.removal.choice", Lists.newArrayList(
			"{{gts_prefix}} &7Your &a{{listing_name}} &7listing has been returned!"
	));
	public static final ConfigKey<List<String>> REMOVAL_EXPIRES = ListKey.of("general.removal.expires", Lists.newArrayList(
			"{{gts_prefix}} &7Your &a{{listing_name}} &7listing has expired, and has thus been returned!"
	));
	public static final ConfigKey<List<String>> MIN_PRICE_ERROR = ListKey.of("general.prices.min-price.invalid", Lists.newArrayList(
			"{{gts_error}} &7In order to sell your &a{{listing_name}}&7, you need to list it for the price of &e{{min_price}}&7..."
	));

	// Items
	public static final ConfigKey<String> UI_ITEMS_NEXT_PAGE = StringKey.of("item-displays.next-page", "&a\u2192 Next Page \u2192");
	public static final ConfigKey<String> UI_ITEMS_LAST_PAGE = StringKey.of("item-displays.last-page", "&c\u2190 Last Page \u2190");
	public static final ConfigKey<String> UI_ITEMS_PLAYER_TITLE = StringKey.of("item-displays.head.title", "&ePlayer Info");
	public static final ConfigKey<List<String>> UI_ITEMS_PLAYER_LORE = ListKey.of("item-displays.head.lore", Lists.newArrayList());
	public static final ConfigKey<String> UI_ITEMS_SORT_TITLE = StringKey.of("item-displays.sort.title", "&eSort Listings");
	public static final ConfigKey<List<String>> UI_ITEMS_SORT_LORE = ListKey.of("item-displays.sort.lore", Lists.newArrayList());
	public static final ConfigKey<String> UI_ITEMS_PLAYER_LISTINGS_TITLE = StringKey.of("item-displays.player-listings.title", "&eYour Listings");
	public static final ConfigKey<List<String>> UI_ITEMS_PLAYER_LISTINGS_LORE = ListKey.of("item-displays.player-listings.lore", Lists.newArrayList());

	// Entries
	public static final ConfigKey<List<String>> ENTRY_INFO = ListKey.of("entries.base-info", Lists.newArrayList(
			"",
			"&7Price: &e{{price}}",
			"&7Time Left: &e{{time_left}}"
	));
	public static final ConfigKey<List<String>> AUCTION_INFO = ListKey.of("entries.auction-info", Lists.newArrayList(
			"",
			"&7High Bidder: &e{{high_bidder}}",
			"&7Current Price: &e{{auc_price}}",
			"&7Increment: &e{{increment}}",
			"&7Time Left: &e{{time_left}}"
	));


	// Pokemon Entries
	public static final ConfigKey<String> POKEMON_ENTRY_SPEC_TEMPLATE = StringKey.of("entries.pokemon.spec-template", "{{ability:s}}{{ivs_percent:s}}{{ivs_stat:s}}{{shiny:s}}&a{{pokemon}}");
	public static final ConfigKey<String> POKEMON_ENTRY_SPEC_TEMPLATE_EGG = StringKey.of("entries.pokemon.egg-spec-template", "&a{{pokemon}}");
	public static final ConfigKey<String> POKEMON_ENTRY_BASE_TITLE = StringKey.of("entries.pokemon.base.title", "&e{{pokemon}} {{shiny:s}}&7| &bLvl {{level}}");
	public static final ConfigKey<List<String>> POKEMON_ENTRY_BASE_LORE = ListKey.of("entries.pokemon.base.lore.base", Lists.newArrayList(
			"&7Seller: &e{{seller}}",
			"",
			"&7Ability: &e{{ability}}",
			"&7Gender: &e{{gender}}",
			"&7Nature: &e{{nature}}",
			"&7Size: &e{{growth}}"
	));
	public static final ConfigKey<List<String>> POKEMON_ENTRY_BASE_MEW_CLONES = ListKey.of("entries.pokemon.base.lore.mew-clones", Lists.newArrayList(
			"&7Clones: &e{{clones}}"
	));
	public static final ConfigKey<List<String>> POKEMON_ENTRY_BASE_LAKE_TRIO = ListKey.of("entries.pokemon.base.lore.lake-trio", Lists.newArrayList(
			"&7Gemmed: &e{{enchanted}}"
	));
	public static final ConfigKey<List<String>> POKEMON_UNBREEDABLE = ListKey.of("entries.pokemon.base.lore.unbreedable", Lists.newArrayList(
			"{{unbreedable}}"
	));
	public static final ConfigKey<List<String>> POKEMON_AURA = ListKey.of("entries.pokemon.base.lore.aura", Lists.newArrayList(
			"{{aura_name}}"
	));
	public static final ConfigKey<List<String>> POKEMON_TEXTURE = ListKey.of("entries.pokemon.base.lore.texture", Lists.newArrayList(
			"{{texture}}"
	));
	public static final ConfigKey<String> POKEMON_ENTRY_CONFIRM_TITLE = StringKey.of("entries.pokemon.confirm.title", "&ePurchase {{pokemon}}?");
	public static final ConfigKey<List<String>> POKEMON_ENTRY_CONFIRM_LORE = ListKey.of("entries.pokemon.confirm.lore", Lists.newArrayList(
			"&7Here's some additional info:",
			"&7EVs: &e{{evs_total}}&7/&e510 &7(&a{{evs_percent}}&7)",
			"&7IVs: &e{{ivs_total}}&7/&e186 &7(&a{{ivs_percent}}&7)",
			"",
			"&7Move Set:",
			"  &7 - &e{{moves_1}}",
			"  &7 - &e{{moves_2}}",
			"  &7 - &e{{moves_3}}",
			"  &7 - &e{{moves_4}}"
	));

	// Error messages
	public static final ConfigKey<List<String>> NOT_ENOUGH_FUNDS = ListKey.of("general.purchase.not-enough-funds", Lists.newArrayList("&cUnfortunately, you were unable to afford the price of {{price}}"));
	public static final ConfigKey<List<String>> ALREADY_CLAIMED = ListKey.of("general.purchase.already-claimed", Lists.newArrayList("&cUnfortunately, this listing has already been claimed..."));
	public static final ConfigKey<List<String>> ITEM_ENTRY_BASE_LORE = ListKey.of("entries.item.base.lore", Lists.newArrayList(
			"&7Seller: &e{{seller}}"
	));
	public static final ConfigKey<List<String>> ITEM_ENTRY_CONFIRM_LORE = ListKey.of("entries.item.confirm.lore", Lists.newArrayList(
			"&7Seller: &e{{seller}}"
	));
	public static final ConfigKey<String> ITEM_ENTRY_CONFIRM_TITLE = StringKey.of("entries.item.confirm.title", "&ePurchase {{item_title}}?");
	public static final ConfigKey<String> ITEM_ENTRY_BASE_TITLE = StringKey.of("entries.item.base.title", "{{item_title}}");
	public static final ConfigKey<String> ITEM_ENTRY_SPEC_TEMPLATE = StringKey.of("entries.item.spec-template", "{{item_title}}");
	public static final ConfigKey<String> ITEM_ENTRY_CONFIRM_TITLE_AUCTION = StringKey.of("entries.item.confirm.title-auctions", "&eBid on {{item_title}}?");
	public static final ConfigKey<List<String>> ITEM_ENTRY_CONFIRM_LORE_AUCTION = ListKey.of("entries.item.confirm.lore-auction", Lists.newArrayList(
			"&7Seller: &e{{seller}}"
	));
	public static final ConfigKey<String> POKEMON_ENTRY_CONFIRM_TITLE_AUCTION = StringKey.of("entries.pokemon.confirm.title-auction", "&eBid on {{pokemon}}?");
	public static final ConfigKey<List<String>> POKEMON_ENTRY_CONFIRM_LORE_AUCTION = ListKey.of("entries.pokemon.confirm.lore-auction", Lists.newArrayList("&7Here's some additional info:",
			"&7EVs: &e{{evs_total}}&7/&e510 &7(&a{{evs_percent}}&7)",
			"&7IVs: &e{{ivs_total}}&7/&e186 &7(&a{{ivs_percent}}&7)",
			"",
			"&7Move Set:",
			"  &7 - &e{{moves_1}}",
			"  &7 - &e{{moves_2}}",
			"  &7 - &e{{moves_3}}",
			"  &7 - &e{{moves_4}}"
	));

	// Logs
	public static final ConfigKey<List<String>> LOGS_ADD = ListKey.of("logging.add-listing", Lists.newArrayList(
			"&7List Price: &e{{price}}"
	));
	public static final ConfigKey<List<String>> LOGS_REMOVE = ListKey.of("logging.remove-listing", Lists.newArrayList(
			"&7Element: &e{{listing_specifics}}"
	));
	public static final ConfigKey<List<String>> LOGS_EXPIRE = ListKey.of("logging.listing-expires", Lists.newArrayList(
			"&7Element: &e{{listing_specifics}}"
	));
	public static final ConfigKey<List<String>> LOGS_PURCHASE = ListKey.of("logging.purchase-listing", Lists.newArrayList(
			"&7Seller: &e{{seller}}",
			"&7Price: &e{{price}}",
			"&7Received: &e{{listing_specifics}}"
	));
	public static final ConfigKey<List<String>> LOGS_SELL = ListKey.of("logging.sell-listing", Lists.newArrayList(
			"&7Buyer: &e{{buyer}}",
			"&7Price: &e{{price}}",
			"&7Sold: &e{{listing_specifics}}"
	));
	public static final ConfigKey<String> LOGS_LOG_DISPLAY = StringKey.of("logging.display", "&e{{action}} &7(&aHover for Info&7) - Issued: &e{{issued}}");
	public static final ConfigKey<List<String>> LOGS_ENTRIES_POKEMON = ListKey.of("logging.entries.pokemon", Lists.newArrayList(
			"&7Element Type: &ePokemon",
			"",
			"&aDetails:",
			"  &7Species: &e{{pokemon}}",
			"  &7Level: &e{{level}}",
			"  &7Shiny: &e{{shiny_state}}",
			"  &7Pokerus: &e{{pokerus_state}}",
			"  &7Ability: &e{{ability}}",
			"  &7Nature: &e{{nature}}",
			"  &7Growth: &e{{growth}}",
			"  &7Gender: &e{{gender}}",
			"  &7EVs: &e{{evs_total}}&7/&e510 &7(&a{{evs_percent}}&7)",
			"  &7EVs: &e{{evhp}}&7/&e{{evatk}}&7/&e{{evdef}}&7/&e{{evspatk}}&7/&e{{evspdef}}&7/&e{{evspeed}}",
			"  &7IVs: &e{{ivs_total}}&7/&e186 &7(&a{{ivs_percent}}&7)",
			"  &7IVs: &e{{ivhp}}&7/&e{{ivatk}}&7/&e{{ivdef}}&7/&e{{ivspatk}}&7/&e{{ivspdef}}&7/&e{{ivspeed}}"
	));

	private static Map<String, ConfigKey<?>> KEYS = null;

	@Override
	public synchronized Map<String, ConfigKey<?>> getAllKeys() {
		if(KEYS == null) {
			Map<String, ConfigKey<?>> keys = new LinkedHashMap<>();

			try {
				Field[] values = MsgConfigKeys.class.getFields();
				for(Field f : values) {
					if(!Modifier.isStatic(f.getModifiers()))
						continue;

					Object val = f.get(null);
					if(val instanceof ConfigKey<?>)
						keys.put(f.getName(), (ConfigKey<?>) val);
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}

			KEYS = ImmutableMap.copyOf(keys);
		}

		return KEYS;
	}
}
