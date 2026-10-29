# Психиатрическая клиника (mindclinic) — Fabric, Minecraft 26.3

Расстройства — это эффекты (иконка + название): Депрессия, СДВГ, Шизофрения,
Тревожное расстройство, Бессонница, ОКР, Биполярное расстройство, РАС.

- Диагностическая панель (ПКМ) — случайный диагноз на 2 минуты.
- Таблетка (ПКМ) — снимает все расстройства.
- /disease give <игрок> <болезнь> [секунд], /disease cure <игрок> [болезнь|all], /disease list
- /effect give @s mindclinic:depression 60 тоже работает.

## Сборка
1. Откройте https://fabricmc.net/develop, выберите Minecraft 26.3 и впишите актуальные
   loader_version, loom_version, fabric_version в gradle.properties
   (если id плагина Loom в генераторе другой — возьмите его в build.gradle).
2. Нужна JDK 25.
3. ./gradlew build -> jar в build/libs/

Текстуры нарисованы с нуля (gen/textures.py), чужие ассеты не используются.

## Сборка через GitHub (без установки Java)
1. Создайте репозиторий на github.com и загрузите в него все файлы проекта (включая папку .github).
2. Впишите актуальные версии в gradle.properties (см. выше).
3. Вкладка Actions -> build -> Run workflow. Через пару минут внизу страницы запуска, в разделе Artifacts, будет mindclinic-jar.
   Внутри два jar: берите тот, что без -sources.
