package com.nickimpact.gts.entries.pixelmon;

import com.google.common.collect.Lists;
import com.nickimpact.gts.GTS;
import com.nickimpact.gts.GTSInfo;
import com.nickimpact.gts.api.GtsService;
import com.nickimpact.gts.api.exceptions.InvalidNBTException;
import com.nickimpact.gts.api.json.Typing;
import com.nickimpact.gts.api.listings.Listing;
import com.nickimpact.gts.api.listings.entries.Entry;
import com.nickimpact.gts.api.listings.entries.Minable;
import com.nickimpact.gts.api.listings.pricing.Price;
import com.nickimpact.gts.api.listings.pricing.PricingException;
import com.nickimpact.gts.api.utils.MessageUtils;
import com.nickimpact.gts.configuration.ConfigKeys;
import com.nickimpact.gts.configuration.MsgConfigKeys;
import com.nickimpact.gts.entries.prices.MoneyPrice;
import com.nickimpact.impactor.api.commands.SpongeCommand;
import com.nickimpact.impactor.api.commands.SpongeSubCommand;
import com.nickimpact.impactor.api.commands.annotations.Aliases;
import com.nickimpact.impactor.api.commands.annotations.Permission;
import com.nickimpact.impactor.api.plugins.SpongePlugin;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.NbtKeys;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import com.pixelmonmod.pixelmon.util.helpers.SpriteHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.common.item.inventory.util.ItemStackUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * (Some note will go here)
 *
 * @author NickImpact
 */
@Typing("Pokemon")
public class PokemonEntry extends Entry<Pokemon> implements Minable {

	private static final PokemonSpec UNTRADABLE = new PokemonSpec("untradeable");

	public PokemonEntry() {
		super();
	}

	public PokemonEntry(EntityPixelmon pokemon, Price price) {
		this(new Pokemon(pokemon), price);
	}

	public PokemonEntry(Pokemon pokemon, Price price) {
		super(pokemon, price);
	}

	@Override
	public SpongeSubCommand commandSpec(boolean isAuction) {
		return new PokemonSub(GTS.getInstance(), isAuction);
	}

	@Override
	public String getSpecsTemplate() {
		List<String> template = Lists.newArrayList();
		if(this.getEntry().getPokemon().isEgg) {
			return GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_SPEC_TEMPLATE_EGG);
		}
		return GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_SPEC_TEMPLATE);
	}

	@Override
	public List<String> getLogTemplate() {
		return GTS.getInstance().getMsgConfig().get(MsgConfigKeys.LOGS_ENTRIES_POKEMON);
	}

	@Override
	public String getName() {
		return this.getEntry().getPokemon().getName();
	}

	@Override
	public ItemStack baseItemStack() {
		return getPicture(this.getEntry().getPokemon());
	}

	@Override
	public String baseTitleTemplate() {
		return GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_BASE_TITLE);
	}

	@Override
	public List<String> baseLoreTemplate(boolean auction) {
		List<String> template = Lists.newArrayList();

		NBTTagCompound nbt = new NBTTagCompound();
		this.getEntry().getPokemon().writeToNBT(nbt);
		String texture = nbt.getString(NbtKeys.CUSTOM_TEXTURE);

		if(new PokemonSpec("unbreedable").matches(this.getEntry().getPokemon())){
			template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_UNBREEDABLE));
		}
		if(!texture.isEmpty()){
			template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_TEXTURE));
		}
		Key<Value<String>> idKey = (Key<Value<String>>)((Entity)this.getEntry().getPokemon()).getKeys().stream().filter(key -> key.getId().equals("entity-particles:id")).findFirst().orElse(null);
		if (idKey != null) {
			String key = ((Entity)this.getEntry().getPokemon()).get(idKey).orElse(null);
			if(!(key == null) || !(key.length() == 1)) {
				template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_AURA));
			}
		}
		template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_BASE_LORE));

		if(this.getEntry().getPokemon().getSpecies().equals(EnumPokemon.Mew)) {
			template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_BASE_MEW_CLONES));
		} else if(LakeTrio.isMember(this.getEntry().getPokemon().getSpecies())) {
			template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_BASE_LAKE_TRIO));
		}

		if(auction) {
			template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.AUCTION_INFO));
		} else {
			template.addAll(GTS.getInstance().getMsgConfig().get(MsgConfigKeys.ENTRY_INFO));
		}

		return template;
	}

	@Override
	public ItemStack confirmItemStack() {
		return ItemStack.builder().itemType(ItemTypes.PAPER).build();
	}

	@Override
	protected String confirmTitleTemplate(boolean auction) {
		return !auction ? GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_CONFIRM_TITLE) :
				GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_CONFIRM_TITLE_AUCTION);
	}

	@Override
	protected List<String> confirmLoreTemplate(boolean auction) {
		return !auction ? GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_CONFIRM_LORE) :
				GTS.getInstance().getMsgConfig().get(MsgConfigKeys.POKEMON_ENTRY_CONFIRM_LORE_AUCTION);
	}

	@Override
	public boolean supportsOffline() {
		return false;
	}

	@Override
	public boolean giveEntry(User user) {
		Optional<PlayerStorage> optStorage = PixelmonStorage.pokeBallManager.getPlayerStorageFromUUID(
				(MinecraftServer) Sponge.getServer(),
				user.getUniqueId()
		);

		if (!optStorage.isPresent())
			return false;

		optStorage.get().addToParty(this.getEntry().getPokemon());
		optStorage.get().sendUpdatedList();

		return true;
	}

	@Override
	public boolean doTakeAway(Player player) {
		if(BattleRegistry.getBattle((EntityPlayer) player) != null) {
			player.sendMessage(Text.of(GTSInfo.ERROR, TextColors.GRAY, "You are in battle, you can't sell any pokemon currently..."));
			return false;
		}

		if(UNTRADABLE.matches(this.getEntry().getPokemon())) {
			player.sendMessage(Text.of(GTSInfo.ERROR, TextColors.GRAY, "This pokemon is marked as untradeable, and cannot be sold..."));
			return false;
		}

		if(GTS.getInstance().getConfig().get(ConfigKeys.BLACKLISTED_POKEMON).stream().anyMatch(name -> name.equalsIgnoreCase(this.getEntry().getPokemon().getName()))){
			player.sendMessage(Text.of(GTSInfo.ERROR, TextColors.GRAY, "Sorry, but ", TextColors.YELLOW, this.getName(), TextColors.GRAY, " has been blacklisted from the GTS..."));
			return false;
		}

		PlayerStorage ps = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP)player).orElse(null);
		if(ps == null)
			return false;

		ps.recallAllPokemon();
		ps.removeFromPartyPlayer(ps.getPosition(this.getEntry().getPokemon().getPokemonId()));
		ps.sendUpdatedList();

		return true;
	}

	private static ItemStack getPicture(EntityPixelmon pokemon) {
		net.minecraft.item.ItemStack item = new net.minecraft.item.ItemStack(PixelmonItems.itemPixelmonSprite);
		NBTTagCompound nbt = new NBTTagCompound();
		String idValue = String.format("%03d", pokemon.baseStats.nationalPokedexNumber);
		if (pokemon.isEgg) {
			switch (pokemon.getSpecies()) {
				case Manaphy:
				case Togepi:
					nbt.setString(NbtKeys.SPRITE_NAME,
					              String.format("pixelmon:sprites/eggs/%s1", pokemon.getSpecies().name.toLowerCase()));
					break;
				default:
					nbt.setString(NbtKeys.SPRITE_NAME, "pixelmon:sprites/eggs/egg1");
					break;
			}
		} else if (pokemon.getIsShiny()) {
			nbt.setString(NbtKeys.SPRITE_NAME, "pixelmon:sprites/shinypokemon/" + idValue + SpriteHelper.getSpriteExtra(pokemon.getSpecies().name, pokemon.getForm()));
		} else {
			nbt.setString(NbtKeys.SPRITE_NAME, "pixelmon:sprites/pokemon/" + idValue + SpriteHelper.getSpriteExtra(pokemon.getSpecies().name, pokemon.getForm()));
		}

		item.setTagCompound(nbt);
		return ItemStackUtil.fromNative(item);
	}

	@Override
	public MoneyPrice calcMinPrice() throws PricingException {
		MoneyPrice price = new MoneyPrice(GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_BASE));
		EntityPixelmon pokemon = this.getEntry().getPokemon();
		boolean isLegend = EnumPokemon.legendaries.contains(pokemon.getName());
		if(isLegend && pokemon.getIsShiny()) {
			price.add(new MoneyPrice(GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_LEGEND) + GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_SHINY)));
		} else if(isLegend) {
			price.add(new MoneyPrice(GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_LEGEND)));
		} else if(pokemon.getIsShiny()) {
			price.add(new MoneyPrice(GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_SHINY)));
		}

		for(int iv : pokemon.stats.ivs.getArray()) {
			if(iv >= GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_IVS_MINVAL)) {
				price.add(new MoneyPrice(GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_IVS_PRICE)));
			}
		}

		if(pokemon.getAbilitySlot() == 2) {
			price.add(new MoneyPrice(GTS.getInstance().getConfig().get(ConfigKeys.MIN_PRICING_POKEMON_HA)));
		}

		Optional<List<Function<Object, Price>>> extras = Sponge.getServiceManager().provideUnchecked(GtsService.class).getMinPriceOptions(this.getClass());
		if(extras.isPresent()) {
			for(Function<Object, Price> function : extras.get()) {
				Price p = function.apply(pokemon);
				if(p instanceof MoneyPrice) {
					price.add((MoneyPrice) function.apply(pokemon));
				} else {
					GTS.getInstance().getConsole().ifPresent(console -> console.sendMessage(Text.of(
							GTSInfo.WARNING, "Pricing other than MoneyPrice are not yet supported for min price calculation..."
					)));
				}
			}
		}

		return price;
	}

	@Aliases({"pokemon", "poke"})
	@Permission(prefix = "sell")
	public class PokemonSub extends SpongeSubCommand {

		private final Text argPos = Text.of("pos");
		private final Text argPrice = Text.of("price");

		private final boolean isAuction;
		private final Text argIncrement = Text.of("increment");

		public PokemonSub(SpongePlugin plugin, boolean isAuction) {
			super(plugin);
			this.isAuction = isAuction;
		}

		@Override
		public CommandElement[] getArgs() {
			return new CommandElement[]{
					GenericArguments.integer(argPos),
					GenericArguments.integer(argPrice),
					GenericArguments.optional(GenericArguments.doubleNum(argIncrement))
			};
		}

		@Override
		public Text getDescription() {
			return Text.of("Handles pokemon");
		}

		@Override
		public Text getUsage() {
			return Text.of("/gts sell/auc pokemon <party slot> <price> (increment - auctions only)");
		}

		@Override
		public SpongeCommand[] getSubCommands() {
			return new SpongeCommand[0];
		}

		@Override
		public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
			if(!GTS.getInstance().getConfig().get(ConfigKeys.POKEMON_ENABLED)) {
				throw new CommandException(Text.of("The selling of pokemon is disabled..."));
			}

			if(!isAuction && args.hasAny(argIncrement)) {
				throw new CommandException(Text.of("Too many arguments..."));
			}

			if(src instanceof Player) {
				Player player = (Player)src;
				int pos = args.<Integer>getOne(argPos).get() - 1;
				int price = args.<Integer>getOne(argPrice).get();
				if(price <= 0) {
					throw new CommandException(Text.of("Price must be a positive integer..."));
				}

				Optional<PlayerStorage> optStorage = PixelmonStorage.pokeBallManager.getPlayerStorage((EntityPlayerMP)player);
				if(optStorage.isPresent()) {
					PlayerStorage storage = optStorage.get();
					NBTTagCompound nbt = storage.getNBT(storage.getIDFromPosition(pos));
					if(nbt != null) {
						EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityFromNBT(
								nbt,
								(World) player.getWorld()
						);
						if (storage.countTeam() == 1)
							throw new CommandException(Text.of("You can't sell your last non-egg party member..."));

						Listing.Builder lb;
						try {
							MoneyPrice mp = new MoneyPrice(price);
							if(!mp.isLowerOrEqual()) {
								throw new CommandException(Text.of("Your money request is above the max amount of " + new MoneyPrice(mp.getMax()).getText()));
							}
							lb = Listing.builder()
									.player(player)
									.entry(new PokemonEntry(pokemon, mp))
									.doesExpire()
									.expiration(
											!isAuction ? GTS.getInstance().getConfig().get(ConfigKeys.LISTING_TIME) :
													GTS.getInstance().getConfig().get(ConfigKeys.AUC_TIME)
									);
						} catch (InvalidNBTException e) {
							e.writeError();
							throw new CommandException(Text.of("Due to an error, your pokemon cannot be listed..."));
						}

						if(isAuction) {
							Optional<Double> optInc = args.getOne(argIncrement);
							if(!optInc.isPresent()) {
								throw new CommandException(Text.of("You must supply an increment..."));
							}
							MoneyPrice increment = new MoneyPrice(optInc.get());
							if(increment.getPrice().compareTo(new BigDecimal(0)) < 0) {
								throw new CommandException(Text.of("Increment must be a positive value..."));
							}
							lb = lb.auction(increment);
						}

						lb.build();

						return CommandResult.success();
					}

					throw new CommandException(Text.of("Unable to find a pokemon in the specified slot..."));
				}

				MessageUtils.genAndSendErrorMessage(
						"Pixelmon Storage Access Error",
						"Unable to locate storage for " + player.getName(),
						"Their UUID: " + player.getUniqueId()
				);
				throw new CommandException(Text.of("Unable to find your party data, this error has been reported"));
			}

			throw new CommandException(Text.of("Only players may use this command..."));
		}
	}

	private enum LakeTrio {
		Mesprit(EnumPokemon.Mesprit),
		Azelf(EnumPokemon.Azelf),
		Uxie(EnumPokemon.Uxie);

		private EnumPokemon species;

		LakeTrio(EnumPokemon species) {
			this.species = species;
		}

		public static boolean isMember(EnumPokemon species) {
			for(LakeTrio guardian : values()) {
				if(guardian.species == species) {
					return true;
				}
			}

			return false;
		}
	}
}
