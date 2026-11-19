// Original binary: sg/ec/dn46hqRSw8KpMNZA
package sg.ec;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.ArrayList;
import net.minecraft.client.multiplayer.ClientSuggestionProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

// $VF: Compiled from s
@CommandInfo(name = "notrace", description = "")
public class NotraceCommand extends AbstractCommand {
   public final ArrayList<Block> field18407 = new ArrayList<>();
   private static final String field18408 = "add";
   private static final String field18409 = "name";
   private static final String field18410 = "remove";
   private static final String field18411 = "name";
   private static final String field18412 = "list";
   private static final String field18413 = "clear";
   private static final String field18414 = "List was cleared.";
   private static final String field18415 = "NoTrace";
   private static final String field18416 = "Block list is empty.";
   private static final String field18417 = "Block list:";
   private static final String field18418 = "block.minecraft.";
   private static final String field18419 = "name";
   private static final String field18420 = "block.minecraft.";
   private static final String field18421 = "block.minecraft.";
   private static final String field18422 = "NoTrace";
   private static final String field18423 = "name";
   private static final String field18424 = "block.minecraft.";
   private static final String field18425 = "block.minecraft.";
   private static final String field18426 = "NoTrace";

   @Override
   public void method00273(LiteralArgumentBuilder<ClientSuggestionProvider> var1) {
      var1.then(this.method01188(field18408).then(this.method01184(field18409, new BlockStateArgument()).executes(var1x -> {
         BlockState var2 = (BlockState)var1x.getArgument(field18423, BlockState.class);
         Block sx = var2.getBlock();
         if (this.field18407.contains(sx)) {
            this.method01187("Block named: " + sx.getDescriptionId().replace(field18424, "") + " is already in the list.");
            return 1;
         } else {
            this.field18407.add(sx);
            this.method01187("Block named: " + sx.getDescriptionId().replace(field18425, "") + " was added to the list.");
            CustomResourceManager.INSTANCE.method02465(field18426);
            return 1;
         }
      })));
      var1.then(this.method01188(field18410).then(this.method01184(field18411, new NoTraceBlockArgument()).executes(var1x -> {
         Block var2 = (Block)var1x.getArgument(field18419, Block.class);
         if (!this.field18407.contains(var2)) {
            this.method01187("Block named: " + var2.getDescriptionId().replace(field18420, "") + " is not in the list.");
            return 1;
         } else {
            this.field18407.remove(var2);
            this.method01187("Block named: " + var2.getDescriptionId().replace(field18421, "") + " was removed from the list.");
            CustomResourceManager.INSTANCE.method02465(field18422);
            return 1;
         }
      })));
      var1.then(this.method01188(field18412).executes(var1x -> {
         if (this.field18407.isEmpty()) {
            this.method01187(field18416);
            return 1;
         }

         this.method01187(field18417);

         for (Block var3 : this.field18407) {
            this.method01187(var3.getDescriptionId().replace(field18418, ""));
         }

         return 1;
      }));
      var1.then(this.method01188(field18413).executes(var1x -> {
         this.field18407.clear();
         this.method01187(field18414);
         CustomResourceManager.INSTANCE.method02465(field18415);
         return 1;
      }));
   }
}
