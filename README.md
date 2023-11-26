# TransitCard

Paper plugin for transit cards in Minecraft

Version: 1.19.4+

Dependencies: [Vault](https://www.spigotmc.org/resources/vault.34315/)

## How to use
- Run /metro_card or any similar command

### Permissions
- `transit_card.buy` - for accessing `/buy_card` command
- `transit_card.buy.[transit]` - for buying `[transit X]` (replace `[transit]` with proper transit type)

### Config

Self explanatory

```toml
# Do you want to not give piece of paper to players
electronic_card = true

# Do you want to give players spyglass for easier signs reading
give_spyglass = true

[prices]
    bus = 0.5
    cable_car = 1.5
    ferry = 2.0
    metro = 1.0
    monorail = 1.0
    tram = 1.0
```

## How to compile
- Download `Minecraft Development` plugin in `IntelliJ IDEA`
- Build artifact, don't forget to modify path

[Full video tutorial (you won't need everything)](https://www.youtube.com/watch?v=5DBJcz0ceaw)

*You can also just download `TransitCard.jar` file*
