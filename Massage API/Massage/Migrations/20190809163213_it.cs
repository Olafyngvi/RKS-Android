using System;
using Microsoft.EntityFrameworkCore.Metadata;
using Microsoft.EntityFrameworkCore.Migrations;

namespace Massage.Migrations
{
    public partial class it : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.CreateTable(
                name: "korisnici",
                columns: table => new
                {
                    Id = table.Column<long>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    Ime = table.Column<string>(nullable: true),
                    Lozinka = table.Column<string>(nullable: true),
                    BrojTelefona = table.Column<string>(nullable: true)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_korisnici", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "termini",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    Termin = table.Column<string>(nullable: true),
                    Vrijeme = table.Column<DateTime>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_termini", x => x.Id);
                });

            migrationBuilder.CreateTable(
                name: "rezervacije",
                columns: table => new
                {
                    Id = table.Column<int>(nullable: false)
                        .Annotation("SqlServer:ValueGenerationStrategy", SqlServerValueGenerationStrategy.IdentityColumn),
                    DatumRezervacije = table.Column<DateTime>(nullable: false),
                    UserId = table.Column<long>(nullable: false),
                    TerminId = table.Column<int>(nullable: false),
                    Dan = table.Column<int>(nullable: false),
                    Mjesec = table.Column<int>(nullable: false),
                    Godina = table.Column<int>(nullable: false)
                },
                constraints: table =>
                {
                    table.PrimaryKey("PK_rezervacije", x => x.Id);
                    table.ForeignKey(
                        name: "FK_rezervacije_termini_TerminId",
                        column: x => x.TerminId,
                        principalTable: "termini",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                    table.ForeignKey(
                        name: "FK_rezervacije_korisnici_UserId",
                        column: x => x.UserId,
                        principalTable: "korisnici",
                        principalColumn: "Id",
                        onDelete: ReferentialAction.Cascade);
                });

            migrationBuilder.CreateIndex(
                name: "IX_rezervacije_TerminId",
                table: "rezervacije",
                column: "TerminId");

            migrationBuilder.CreateIndex(
                name: "IX_rezervacije_UserId",
                table: "rezervacije",
                column: "UserId");
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropTable(
                name: "rezervacije");

            migrationBuilder.DropTable(
                name: "termini");

            migrationBuilder.DropTable(
                name: "korisnici");
        }
    }
}
