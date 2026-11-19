// Original binary: sg/ec/jtsG13xFyqr7Ozsx
package sg.ec;

import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.gui.Font.GlyphVisitor;
import net.minecraft.client.gui.Font.PreparedText;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.gui.font.TextRenderable.Styled;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer.Submit;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.FormattedCharSequence;

// $VF: Compiled from s
public enum GlyphWidthVisitor implements GlyphVisitor {
   INSTANCE;

   private TextRenderable[] field20564 = new TextRenderable[512];
   private int[] field20565 = new int[512];
   private int[] field20566 = new int[512];
   private int[] field20567 = new int[512];
   private boolean[] field20568 = new boolean[512];
   private int field20569;
   private RenderType[] field20570 = new RenderType[16];
   private int[] field20571 = new int[17];
   private int[] field20572 = new int[16];
   private int field20573;
   private boolean field20574;
   private DisplayMode field20575 = DisplayMode.NORMAL;
   private int field20576;

   public void build(Font var1, List<Submit> var2, WorldVertexConsumerAccess var3, boolean var4) {
      this.field20569 = 0;
      this.field20573 = 0;
      this.field20574 = true;
      FontTextCache var5 = ((FontTextCacheAccess)var1).hexTextCache();
      int var6 = 0;

      for (int var7 = var2.size(); var6 < var7; var6++) {
         Submit var8 = (Submit)var2.get(var6);
         this.field20576 = var6;
         this.field20575 = var8.displayMode();
         prepare(var1, var5, var8).visit(this);
      }

      if (this.field20574 && var4) {
         this.emitGrouped(var2, var3);
      } else {
         this.emitOrdered(var2, var3);
      }

      Arrays.fill(this.field20564, 0, this.field20569, null);
      Arrays.fill(this.field20570, 0, this.field20573, null);
   }

   private static PreparedText prepare(Font var0, FontTextCache var1, Submit var2) {
      FormattedCharSequence var3 = var2.text().getVisualOrderText();
      PreparedText var4 = var1.method01345(var3, var2.x(), var2.y(), var2.color(), false, false, var2.backgroundColor());
      return var4 != null ? var4 : var0.prepareText(var3, var2.x(), var2.y(), var2.color(), false, false, var2.backgroundColor());
   }

   @Override
   public void acceptGlyph(Styled var1) {
      this.add(var1, false);
   }

   @Override
   public void acceptEffect(TextRenderable var1) {
      this.add(var1, true);
   }

   @Override
   public void acceptRenderable(TextRenderable var1) {
      this.add(var1, true);
   }

   private void add(TextRenderable var1, boolean var2) {
      if (this.field20569 == this.field20564.length) {
         int var3 = this.field20569 << 1;
         this.field20564 = Arrays.copyOf(this.field20564, var3);
         this.field20565 = Arrays.copyOf(this.field20565, var3);
         this.field20566 = Arrays.copyOf(this.field20566, var3);
         this.field20568 = Arrays.copyOf(this.field20568, var3);
         this.field20567 = new int[var3];
      }

      this.field20564[this.field20569] = var1;
      this.field20565[this.field20569] = this.typeIndex(var1.renderType(this.field20575));
      this.field20566[this.field20569] = this.field20576;
      this.field20568[this.field20569] = var2;
      this.field20569++;
   }

   private int typeIndex(RenderType var1) {
      for (int var2 = 0; var2 < this.field20573; var2++) {
         if (this.field20570[var2] == var1) {
            return var2;
         }
      }

      if (this.field20573 == this.field20570.length) {
         int var3 = this.field20573 << 1;
         this.field20570 = Arrays.copyOf(this.field20570, var3);
         this.field20571 = new int[var3 + 1];
         this.field20572 = new int[var3];
      }

      this.field20570[this.field20573] = var1;
      this.field20574 = this.field20574 & var1.canConsolidateConsecutiveGeometry();
      return this.field20573++;
   }

   private void emitGrouped(List<Submit> var1, WorldVertexConsumerAccess var2) {
      int[] var3 = this.field20571;
      Arrays.fill(var3, 0, this.field20573 + 1, 0);

      for (int var4 = 0; var4 < this.field20569; var4++) {
         var3[this.field20565[var4] + 1]++;
      }

      for (int var10 = 0; var10 < this.field20573; var10++) {
         var3[var10 + 1] = var3[var10 + 1] + var3[var10];
      }

      int[] var11 = this.field20567;
      int var5 = 0;

      while (var5 < this.field20569) {
         int var6 = this.field20565[var5];
         var11[var3[var6]++] = var5++;
      }

      var5 = 0;

      for (int var13 = 0; var13 < this.field20573; var13++) {
         int var7 = var3[var13];
         if (var5 != var7) {
            VertexConsumer var8 = var2.hexVertexBuilder(this.field20570[var13]);

            for (int var9 = var5; var9 < var7; var9++) {
               this.render(var11[var9], var1, var8);
            }

            var5 = var7;
         }
      }
   }

   private void emitOrdered(List<Submit> var1, WorldVertexConsumerAccess var2) {
      Arrays.fill(this.field20572, 0, this.field20573, 0);
      int var3 = 0;
      int var4 = 0;

      while (var4 < this.field20569) {
         if (this.field20568[var4]) {
            this.render(var4, var1, var2.hexVertexBuilder(this.field20570[this.field20565[var4]]));
            var4++;
         } else {
            int var5 = this.field20566[var4];
            int var6 = var4;

            while (var6 < this.field20569 && !this.field20568[var6] && this.field20566[var6] == var5) {
               var6++;
            }

            var3++;

            for (int var7 = var4; var7 < var6; var7++) {
               int var8 = this.field20565[var7];
               if (this.field20572[var8] != var3) {
                  this.field20572[var8] = var3;
                  VertexConsumer var9 = var2.hexVertexBuilder(this.field20570[var8]);

                  for (int var10 = var7; var10 < var6; var10++) {
                     if (this.field20565[var10] == var8) {
                        this.render(var10, var1, var9);
                     }
                  }
               }
            }

            var4 = var6;
         }
      }
   }

   private void render(int var1, List<Submit> var2, VertexConsumer var3) {
      Submit var4 = (Submit)var2.get(this.field20566[var1]);
      this.field20564[var1].render(var4.pose(), var3, var4.lightCoords(), false);
   }
}
