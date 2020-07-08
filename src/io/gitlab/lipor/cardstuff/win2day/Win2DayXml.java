package io.gitlab.lipor.cardstuff.win2day;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import io.gitlab.lipor.HelperFunctions;
import io.gitlab.lipor.cardstuff.Card;

@XmlRootElement(name = "session")
public class Win2DayXml {
	@XmlElement(name = "game")
	public List<Game> games;

	@Override
	public String toString() {
		return "Win2DayXml [games=" + games + "]";
	}

	public static class Game {
		@XmlAttribute(name = "gamecode")
		public String gamecode;
		@XmlElement(name = "general")
		public General general;
		@XmlElement(name = "round")
		public List<Round> rounds;

		@Override
		public String toString() {
			return "Game [gamecode=" + gamecode + ", general=" + general + ", rounds=" + rounds + "]";
		}

		public static class General {
			@XmlElement(name = "startdate")
			@XmlJavaTypeAdapter(DateAdapter.class)
			public Date startdate;
			@XmlElementWrapper(name = "players")
			@XmlElement(name = "player")
			public List<Player> players;

			@Override
			public String toString() {
				return "General [startdate=" + startdate + ", players=" + players + "]";
			}

			public static class Player {
				@XmlAttribute(name = "name")
				public String name;
				@XmlAttribute(name = "addon")
				public int addonId;
				@XmlAttribute(name = "chips")
				@XmlJavaTypeAdapter(SumAdapter.class)
				public Double chips;
				@XmlAttribute(name = "seat")
				public int seat;
				@XmlAttribute(name = "rebuy")
				public int rebuy;
				@XmlAttribute(name = "reg_code")
				public String regCode;
				@XmlAttribute(name = "win")
				@XmlJavaTypeAdapter(SumAdapter.class)
				public Double win;
				@XmlAttribute(name = "dealer")
				public boolean isDealer;
				@XmlAttribute(name = "bet")
				@XmlJavaTypeAdapter(SumAdapter.class)
				public Double bet;

				@Override
				public String toString() {
					return "Player [name=" + name + ", addonId=" + addonId + ", chips=" + chips + ", seat=" + seat
							+ ", rebuy=" + rebuy + ", regCode=" + regCode + ", win=" + win + ", isDealer=" + isDealer
							+ ", bet=" + bet + "]";
				}
			}

		}

		public static class Round {
			@XmlAttribute(name = "no")
			public int roundNumber;
			@XmlElement(name = "cards")
			public List<Card> cards;
			@XmlElement(name = "action")
			public List<Action> actions;

			@Override
			public String toString() {
				return "Round [roundNumber=" + roundNumber + ", cards=" + cards + ", actions=" + actions + "]";
			}

			public static class Card {
				@XmlAttribute(name = "type")
				public String typeIdentifier;
				@XmlAttribute(name = "player")
				public String playerName;
				@XmlValue()
				@XmlJavaTypeAdapter(CardAdapter.class)
				public List<io.gitlab.lipor.cardstuff.Card> cards;

				@Override
				public String toString() {
					return "Card [typeIdentifier=" + typeIdentifier + ", playerName=" + playerName + ", cards=" + cards
							+ "]";
				}
			}

			public static class Action {
				@XmlAttribute(name = "no")
				public int actionNumber;
				@XmlAttribute(name = "type")
				@XmlJavaTypeAdapter(ActionTypeAdapter.class)
				public Integer typeId;
				@XmlAttribute(name = "player")
				public String playerName;
				@XmlAttribute(name = "sum")
				@XmlJavaTypeAdapter(SumAdapter.class)
				public Double sum;

				@Override
				public String toString() {
					return "Action [actionNumber=" + actionNumber + ", typeId=" + typeId + ", playerName=" + playerName
							+ ", sum=" + sum + "]";
				}
			}
		}

	}

	public static class DateAdapter extends XmlAdapter<String, Date> {
		@Override
		public Date unmarshal(String v) throws Exception {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(v);
		}

		@Override
		public String marshal(Date v) throws Exception {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(v);
		}
	}

	public static class CardAdapter extends XmlAdapter<String, List<io.gitlab.lipor.cardstuff.Card>> {
		@Override
		public List<io.gitlab.lipor.cardstuff.Card> unmarshal(String v) throws Exception {
			if (Objects.equals(v, "X X") || HelperFunctions.isEmpty(v)) {
				return new ArrayList<>();
			}

			return HelperFunctions.stream(v.split(" ")).map(x -> {
				char suitCode = x.charAt(0);
				Integer value = adjustValue(x.substring(1, x.length()));
				try {
					return new io.gitlab.lipor.cardstuff.Card(value, suitCode);
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}).filter(Objects::nonNull).collect(Collectors.toList());
		}

		public static int adjustValue(String value) {
			try {
				if (Integer.valueOf(value) != null) {
					return Integer.valueOf(value);
				}
			} catch (NumberFormatException ignore) {
			}
			switch (value) {
			case "J":
				return Card.JACK;
			case "Q":
				return Card.QUEEN;
			case "K":
				return Card.KING;
			case "A":
				return Card.ACE;
			}
			throw new AssertionError();
		}

		@Override
		public String marshal(List<io.gitlab.lipor.cardstuff.Card> v) throws Exception {
			return "";
		}
	}

	public static class ActionTypeAdapter extends XmlAdapter<String, Integer> {
		@Override
		public Integer unmarshal(String v) throws Exception {
			return ActionType.create(Integer.valueOf(v)).getActionId();

		}

		@Override
		public String marshal(Integer v) throws Exception {
			return "";
		}
	}

	public static class SumAdapter extends XmlAdapter<String, Double> {
		@Override
		public Double unmarshal(String v) throws Exception {
			NumberFormat numberFormatter = DecimalFormat.getInstance(Locale.US);
			DecimalFormatSymbols customSymbol = new DecimalFormatSymbols();
			customSymbol.setDecimalSeparator(',');
			customSymbol.setGroupingSeparator('.');
			((DecimalFormat) numberFormatter).setDecimalFormatSymbols(customSymbol);
			return numberFormatter.parse(v).doubleValue();
		}

		@Override
		public String marshal(Double v) throws Exception {
			return "";
		}
	}

	public enum ActionType {
		CALL(3, io.gitlab.lipor.cardstuff.Action.CALL),
		FOLD(0, io.gitlab.lipor.cardstuff.Action.FOLD),
		CHECK(4, io.gitlab.lipor.cardstuff.Action.CHECK),
		BET(5, io.gitlab.lipor.cardstuff.Action.BET),
		RAISE(23, io.gitlab.lipor.cardstuff.Action.RAISE),
		ANTE(15, io.gitlab.lipor.cardstuff.Action.ANTE),
		SMALLBLIND(1, io.gitlab.lipor.cardstuff.Action.SMALLBLIND),
		BIGBLIND(2, io.gitlab.lipor.cardstuff.Action.BIGBLIND),
		CALL2(7, io.gitlab.lipor.cardstuff.Action.CALL),

		SMALL_AND_BIGBLIND(-1, io.gitlab.lipor.cardstuff.Action.SMALL_AND_BIGBLIND),
		DEAL(-1, io.gitlab.lipor.cardstuff.Action.DEAL),
		COLLECT(-1, io.gitlab.lipor.cardstuff.Action.COLLECT),
		FLOP(-1, io.gitlab.lipor.cardstuff.Action.FLOP),
		TURN(-1, io.gitlab.lipor.cardstuff.Action.TURN),
		RIVER(-1, io.gitlab.lipor.cardstuff.Action.RIVER),
		SHOW(-1, io.gitlab.lipor.cardstuff.Action.SHOW),
		MUCK(-1, io.gitlab.lipor.cardstuff.Action.MUCK),
		SAY(-1, io.gitlab.lipor.cardstuff.Action.SAY),
		SHOWDOWN(-1, io.gitlab.lipor.cardstuff.Action.SHOWDOWN),
		UNCALLED_BET(-1, io.gitlab.lipor.cardstuff.Action.UNCALLED_BET),;

		private int xmlId;
		private int actionId;

		public int getActionId() {
			return actionId;
		}

		ActionType(int xmlId, int actionId) {
			this.xmlId = xmlId;
			this.actionId = actionId;
		}

		public static ActionType create(int xmlId) {
			return HelperFunctions.stream(values()).filter(x -> x.xmlId == xmlId).findFirst().orElseThrow(() -> new IllegalArgumentException());
		}
	}
}
