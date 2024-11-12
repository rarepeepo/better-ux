//css_import util
using System;
using System.IO;
using System.Text;
using System.Text.RegularExpressions;
using System.Linq;
using static Script.Util;

namespace Script {
    class Program {
        static void Main (string[] args) {
            var dir_out = @"C:\Users\rarepeepo\Desktop\Java\mc_src";
            var dir_in  = @"C:\Users\rarepeepo\Desktop\src";

            foreach(var f in Directory.GetFiles(dir_in, "*.*")) {
                var blob = File.ReadAllText(f).Replace('\0', ' ');
                var m = Regex.Match(blob, @"((?:net/minecraft|com/mojang).*?)(?=SRC)");
                if (!m.Success)
                    throw new Exception(blob);
                var path = Path.Combine(dir_out,
                    m.Groups[1].Value.Replace('/', '\\') + ".java");
                WriteFixedFile(f, path);
            }
        }

        static void WriteFixedFile(string inputPath, string outputPath) {
            var lines = File.ReadAllLines(inputPath, Encoding.ASCII)
                .Select(l => Regex.Replace(l, @"[^\u0020-\u007F]+", string.Empty)).ToList();
            var start = -1;
            var end = lines.Count - 1;

            for(var i = 0; i < lines.Count; i++) {
                if(start == -1 && lines[i].Contains("/*"))
                    start = i + 1;
                if (lines[i].StartsWith("LNUM")) {
                    end = i - 1;
                    break;
                }
            }

            if (start == -1)
                throw new Exception($"start not found: {inputPath}");
            if (end == -1)
                throw new Exception($"end not found: {inputPath}");
            
            var dir = Path.GetDirectoryName(outputPath);
            Directory.CreateDirectory(dir);
            using(var sw = new StreamWriter(outputPath)) {
                sw.WriteLine("/*");
                for (var i = start; i < end; i++)
                    sw.WriteLine(lines[i]);
            }
        }
    }
}
